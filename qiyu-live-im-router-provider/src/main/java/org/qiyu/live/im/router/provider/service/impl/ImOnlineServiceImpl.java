package org.qiyu.live.im.router.provider.service.impl;

import jakarta.annotation.Resource;
import org.qiyu.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.qiyu.live.im.router.provider.service.ImOnlineService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: QingY
 * @Date: Created in 14:28 2024-05-08
 * @Description:
 */
@Service
public class ImOnlineServiceImpl implements ImOnlineService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isOnline(long userId, int appId) {
        return redisTemplate.hasKey(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId);
    }
}
