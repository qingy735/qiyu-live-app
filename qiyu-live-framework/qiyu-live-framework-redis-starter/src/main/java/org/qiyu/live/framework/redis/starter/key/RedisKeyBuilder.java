package org.qiyu.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: QingY
 * @Date: Created in 17:01 2024-04-08
 * @Description:
 */
public class RedisKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;
    private static final String SPLIT_ITEM = ":";

    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }

}
