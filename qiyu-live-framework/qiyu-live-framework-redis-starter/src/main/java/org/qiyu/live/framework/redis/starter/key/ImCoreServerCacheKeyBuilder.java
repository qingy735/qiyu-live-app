package org.qiyu.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: QingY
 * @Date: Created in 17:04 2024-04-08
 * @Description:
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class ImCoreServerCacheKeyBuilder extends RedisKeyBuilder {

    private static String IM_ONLINE_ZSET = "imOnlineZset";

    /**
     * 按照用户id取模10000 得出具体缓存所在key
     *
     * @param userId
     * @return
     */
    public String buildImHeartBeatKey(Integer appId, Long userId) {
        return super.getPrefix() + IM_ONLINE_ZSET + super.getSplitItem() + appId + super.getSplitItem() + userId % 10000;
    }

}
