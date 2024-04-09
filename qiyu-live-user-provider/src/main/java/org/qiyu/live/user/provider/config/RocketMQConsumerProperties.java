package org.qiyu.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消费者配置信息
 *
 * @Author: QingY
 * @Date: Created in 21:48 2024-04-08
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "qiyu.rmq.consumer")
public class RocketMQConsumerProperties {
    // rocketMQ的nameServer地址
    private String nameServer;
    // 分组名称
    private String groupName;

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "RocketMQConsumerProperties{" +
                "nameServer='" + nameServer + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
