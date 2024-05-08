package org.qiyu.live.im.provider.service.impl;

import jakarta.annotation.Resource;
import org.qiyu.live.framework.redis.starter.key.ImProviderCacheKeyBuilder;
import org.qiyu.live.im.provider.service.ImTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: QingY
 * @Date: Created in 21:32 2024-04-28
 * @Description:
 */
@Service
public class ImTokenServiceImpl implements ImTokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public String createImLoginToken(long userId, int appId) {
        String token = UUID.randomUUID() + "%" + appId;
        redisTemplate.opsForValue().set(cacheKeyBuilder.buildImLoginToken(token), userId, 5, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        Object userId = redisTemplate.opsForValue().get(cacheKeyBuilder.buildImLoginToken(token));
        return userId == null ? null : Long.valueOf((Integer) userId);
    }
}
