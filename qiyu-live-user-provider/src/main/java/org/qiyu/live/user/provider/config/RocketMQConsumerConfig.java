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
import org.qiyu.live.user.dto.UserDTO;
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
    private UserProviderCacheKeyBuilder providerCacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }

    public void initConsumer() {
        // 初始化RocketMQ消费者
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        try {
            defaultMQPushConsumer.setNamesrvAddr(consumerProperties.getNameServer());
            defaultMQPushConsumer.setConsumerGroup(consumerProperties.getGroupName());
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe("user-update-cache", "*");
            defaultMQPushConsumer.setMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    String msgStr = new String(list.get(0).getBody());
                    UserDTO userDTO = JSON.parseObject(msgStr, UserDTO.class);
                    if (userDTO == null || userDTO.getUserId() == null) {
                        LOGGER.error("用户id为空，参数异常，内容: {}", msgStr);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    // 延迟消息回调 处理相关缓存的二次删除
                    redisTemplate.delete(providerCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()));
                    LOGGER.info("延迟删除处理，userDTO is {}", userDTO);
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
