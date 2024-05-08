package org.qiyu.live.im.core.server.handler.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.qiyu.live.framework.redis.starter.key.ImCoreServerCacheKeyBuilder;
import org.qiyu.live.im.constants.ImConstants;
import org.qiyu.live.im.constants.ImMsgCodeEnum;
import org.qiyu.live.im.core.server.common.ImContextUtils;
import org.qiyu.live.im.core.server.common.ImMsg;
import org.qiyu.live.im.core.server.handler.SimplyHandler;
import org.qiyu.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.qiyu.live.im.dto.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 心跳消息处理器
 *
 * @Author: QingY
 * @Date: Created in 21:39 2024-04-27
 * @Description:
 */
@Component
public class HeartBeatImMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatImMsgHandler.class);
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImCoreServerCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        // 心跳包校验
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            LOGGER.error("attr error,imMsg is {}", imMsg);
            // 有可能是错误的消息包导致，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        // 心跳包record记录 redis存储
        String redisKey = cacheKeyBuilder.buildImHeartBeatKey(userId, appId);
        // qiyu-live-im-core-server:heartbeat:999:zset
        this.recordOnlineTime(userId, redisKey);
        this.removeExpireRecord(userId, redisKey);
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        // 延长用户保存的ip绑定记录时间
        stringRedisTemplate.expire(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId, ImConstants.DEFAULT_HEART_BEAT_GAP * 2, TimeUnit.SECONDS);
        ImMsgBody msgBody = new ImMsgBody();
        msgBody.setUserId(userId);
        msgBody.setAppId(appId);
        msgBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), JSON.toJSONString(msgBody));
        LOGGER.info("[HeartBeatImMsgHandler] imMsg is {}", imMsg);
        ctx.writeAndFlush(respMsg);
    }

    /**
     * 清理掉过期不在线用户留下的心跳记录（在两次心跳包发送间隔中 没有更新score就会删除）
     *
     * @param userId
     * @param redisKey
     */
    private void removeExpireRecord(Long userId, String redisKey) {
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, System.currentTimeMillis() - ImConstants.DEFAULT_HEART_BEAT_GAP * 1000 * 2);
    }

    /**
     * 记录用户最近一次心跳时间到zSet上
     *
     * @param userId
     * @param redisKey
     */
    private void recordOnlineTime(Long userId, String redisKey) {
        redisTemplate.opsForZSet().add(redisKey, userId, System.currentTimeMillis());
    }
}
