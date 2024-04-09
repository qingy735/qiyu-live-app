package org.qiyu.live.user.provider.config;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * RocketMQ的生产者bean配置类
 *
 * @Author: QingY
 * @Date: Created in 21:36 2024-04-08
 * @Description:
 */
@Configuration
public class RocketMQProducerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RocketMQProducerProperties producerProperties;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public MQProducer mqProducer() {
        ThreadPoolExecutor asyncThreadPollExecutor = new ThreadPoolExecutor(100, 150, 3,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName(applicationName + ":rmp-producer:" + ThreadLocalRandom.current().nextInt(1000));
                return thread;
            }
        });
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        try {
            defaultMQProducer.setNamesrvAddr(producerProperties.getNameServer());
            defaultMQProducer.setProducerGroup(producerProperties.getGroupName());
            defaultMQProducer.setRetryTimesWhenSendFailed(producerProperties.getRetryTimes());
            defaultMQProducer.setRetryTimesWhenSendAsyncFailed(producerProperties.getRetryTimes());
            defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
            // 设置异步发送线程池
            defaultMQProducer.setAsyncSenderExecutor(asyncThreadPollExecutor);
            defaultMQProducer.start();
            LOGGER.info("mq生产者启动成功, nameserver is {}", producerProperties.getNameServer());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return defaultMQProducer;
    }

}
