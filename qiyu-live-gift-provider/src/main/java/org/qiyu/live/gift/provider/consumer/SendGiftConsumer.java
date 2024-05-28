package org.qiyu.live.gift.provider.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.qiyu.live.bank.interfaces.dto.AccountTradeReqDTO;
import org.qiyu.live.bank.interfaces.dto.AccountTradeRespDTO;
import org.qiyu.live.bank.interfaces.rpc.IQiyuCurrencyAccountRpc;
import org.qiyu.live.common.interfaces.dto.SendGiftMq;
import org.qiyu.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import org.qiyu.live.common.interfaces.topic.GiftProviderTopicNames;
import org.qiyu.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import org.qiyu.live.gift.constants.SendGiftTypeEnum;
import org.qiyu.live.im.constants.AppIdEnum;
import org.qiyu.live.im.dto.ImMsgBody;
import org.qiyu.live.im.router.interfaces.constants.ImMsgBizCodeEnum;
import org.qiyu.live.im.router.interfaces.rpc.ImRouterRpc;
import org.qiyu.live.living.interfaces.dto.LivingRoomReqDTO;
import org.qiyu.live.living.interfaces.dto.LivingRoomRespDTO;
import org.qiyu.live.living.interfaces.rpc.ILivingRoomRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 发送礼物消费者
 *
 * @Author idea
 * @Date: Created in 14:28 2023/8/1
 * @Description
 */
@Configuration
public class SendGiftConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendGiftConsumer.class);

    private static final Long PK_MAX_NUM = 1000L;
    private static final Long PK_MIN_NUM = 0L;

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @DubboReference
    private IQiyuCurrencyAccountRpc qiyuCurrencyAccountRpc;
    @DubboReference
    private ImRouterRpc routerRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        // 老版本中会开启，新版本的mq不需要使用到
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + SendGiftConsumer.class.getSimpleName());
        // 一次从broker中拉取10条消息到本地内存当中进行消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 监听礼物缓存数据更新的行为
        mqPushConsumer.subscribe(GiftProviderTopicNames.SEND_GIFT, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                SendGiftMq sendGiftMq = JSON.parseObject(new String(msg.getBody()), SendGiftMq.class);
                String mqConsumerKey = cacheKeyBuilder.buildGiftConsumeKey(sendGiftMq.getUuid());
                Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(mqConsumerKey, -1, 5, TimeUnit.MINUTES);
                if (!lockStatus) {
                    // 曾经消费过
                    continue;
                }
                AccountTradeReqDTO tradeReqDTO = new AccountTradeReqDTO();
                tradeReqDTO.setUserId(sendGiftMq.getUserId());
                tradeReqDTO.setNum(sendGiftMq.getPrice());
                AccountTradeRespDTO tradeRespDTO = qiyuCurrencyAccountRpc.consumeForSendGift(tradeReqDTO);
                JSONObject jsonObject = new JSONObject();
                Integer sendGiftType = sendGiftMq.getType();
                // 如果余额扣减成功
                if (tradeRespDTO.isSuccess()) {
                    if (SendGiftTypeEnum.DEFAULT_SEND_GIFT.getCode().equals(sendGiftType)) {
                        // 触发礼物特效推送功能
                        jsonObject.put("url", sendGiftMq.getUrl());
                        sendImMsgSingleton(sendGiftMq.getReceiverId(), ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_SUCCESS.getCode(), jsonObject);
                    } else if (SendGiftTypeEnum.PK_SEND_GIFT.getCode().equals(sendGiftType)) {
                        // pk类型的送礼 要通知什么给直播间的用户
                        Integer roomId = sendGiftMq.getRoomId();
                        // 进度条全直播间可见
                        String pkNumKey = cacheKeyBuilder.buildLivingPkKey(roomId);
                        Long pkObjId = livingRoomRpc.queryOnlinePkUserId(roomId);
                        LivingRoomRespDTO roomRespDTO = livingRoomRpc.queryByRoomId(roomId);
                        if (pkObjId ==null || roomRespDTO == null || roomRespDTO.getAnchorId() == null) {
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                        Long pkUserId = roomRespDTO.getAnchorId();
                        Long resultNum = 500L;
                        Long pkNum = 0L;
                        String incrKey = cacheKeyBuilder.buildLivingPkSendSeq(roomId);
                        Long sendGiftSeqNum = redisTemplate.opsForValue().increment(incrKey);
                        if (sendGiftMq.getReceiverId().equals(pkUserId)) {
                            resultNum = redisTemplate.opsForValue().increment(pkNumKey, sendGiftMq.getPrice());
                        } else if (sendGiftMq.getReceiverId().equals(pkObjId)) {
                            resultNum = redisTemplate.opsForValue().decrement(pkNumKey, sendGiftMq.getPrice());
                        }
                        pkNum = resultNum;
                        if (PK_MAX_NUM <= resultNum) {
                            jsonObject.put("winnerId", pkUserId);
                            pkNum = PK_MAX_NUM;
                        } else if (PK_MIN_NUM >= resultNum) {
                            jsonObject.put("winnerId", pkObjId);
                            pkNum = PK_MIN_NUM;
                        }
                        jsonObject.put("sendGiftSeqNum", sendGiftSeqNum);
                        jsonObject.put("pkNum", pkNum);
                        // url 礼物特效全直播间可见
                        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
                        livingRoomReqDTO.setRoomId(roomId);
                        List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(livingRoomReqDTO);
                        jsonObject.put("url", sendGiftMq.getUrl());
                        batchSendImMsg(userIdList, ImMsgBizCodeEnum.LIVING_ROOM_PK_SEND_GIFT_SUCCESS.getCode(), jsonObject);
                    }

                } else {
                    // 利用im将发送失败的消息告知用户
                    jsonObject.put("url", tradeRespDTO.getMsg());
                    sendImMsgSingleton(sendGiftMq.getUserId(), ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_FAIL.getCode(), jsonObject);
                }
                LOGGER.info("[SendGiftConsumer] msg is {}", msg);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("mq消费者启动成功,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }

    /**
     * 单独发送im消息
     *
     * @param userId
     * @param bizCode
     * @param jsonObject
     */
    private void sendImMsgSingleton(Long userId, int bizCode, JSONObject jsonObject) {
        ImMsgBody imMsgBody = new ImMsgBody();
        imMsgBody.setAppId(AppIdEnum.QIYU_LIVE_BIZ.getCode());
        imMsgBody.setBizCode(bizCode);
        imMsgBody.setUserId(userId);
        imMsgBody.setData(jsonObject.toJSONString());
        routerRpc.sendMsg(imMsgBody);
    }

    /**
     * 批量发送im消息
     *
     * @param userIdList
     * @param bizCode
     * @param jsonObject
     */
    private void batchSendImMsg(List<Long> userIdList, int bizCode, JSONObject jsonObject) {
        List<ImMsgBody> imMsgBodies = userIdList.stream().map(userId -> {
            ImMsgBody imMsgBody = new ImMsgBody();
            imMsgBody.setAppId(AppIdEnum.QIYU_LIVE_BIZ.getCode());
            imMsgBody.setBizCode(bizCode);
            imMsgBody.setUserId(userId);
            imMsgBody.setData(jsonObject.toJSONString());
            return imMsgBody;
        }).collect(Collectors.toList());
        routerRpc.batchSendMsg(imMsgBodies);
    }
}
