package org.qiyu.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: QingY
 * @Date: Created in 17:18 2024-04-08
 * @Description:
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class OtherProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static String USER_INFO_KEY = "userInfo";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY + super.getSplitItem() + userId;
    }
}
