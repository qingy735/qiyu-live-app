package org.qiyu.live.user.provider.config;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.qiyu.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.qiyu.live.user.constants.CacheAsyncDeleteCode;
import org.qiyu.live.user.constants.UserProviderTopicNames;
import org.qiyu.live.user.dto.UserCacheAsyncDeleteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * RocketMQ的消费者bean配置类
 *
 * @Author: QingY
 * @Date: Created in 21:50 2024-04-08
 * @Description:
 */
@Configuration
public class RocketMQConsumerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RocketMQConsumerProperties consumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }

    public void initConsumer() {
        // 初始化RocketMQ消费者
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        try {
            defaultMQPushConsumer.setNamesrvAddr(consumerProperties.getNameServer());
            defaultMQPushConsumer.setConsumerGroup(consumerProperties.getGroupName() + "_" + RocketMQConsumerConfig.class.getSimpleName());
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC, "*");
            defaultMQPushConsumer.setMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    String msgStr = new String(list.get(0).getBody());
                    UserCacheAsyncDeleteDTO cacheAsyncDeleteDTO = JSON.parseObject(msgStr, UserCacheAsyncDeleteDTO.class);
                    if (cacheAsyncDeleteDTO == null) {
                        LOGGER.error("UserCacheAsyncDeleteDTO对象为空，参数异常，内容: {}", msgStr);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    Long userId = JSON.parseObject(cacheAsyncDeleteDTO.getJson()).getLong("userId");
                    // 延迟消息回调 处理相关缓存的二次删除
                    if (CacheAsyncDeleteCode.USER_INFO_DELETE.getCode() == cacheAsyncDeleteDTO.getCode()) {
                        redisTemplate.delete(cacheKeyBuilder.buildUserInfoKey(userId));
                        LOGGER.info("二次删除成功，业务Code为：{}，userId为: {}", cacheAsyncDeleteDTO.getCode(), userId);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    } else if (CacheAsyncDeleteCode.USER_TAG_DELETE.getCode() == cacheAsyncDeleteDTO.getCode()) {
                        redisTemplate.delete(cacheKeyBuilder.buildTagKey(userId));
                        LOGGER.info("二次删除成功，业务Code为：{}，userId为: {}", cacheAsyncDeleteDTO.getCode(), userId);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
            LOGGER.info("mq消费者启动成功, nameserver is {}", consumerProperties.getNameServer());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }

}
