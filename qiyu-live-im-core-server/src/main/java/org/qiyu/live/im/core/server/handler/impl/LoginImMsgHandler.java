package org.qiyu.live.im.core.server.handler.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.qiyu.live.im.constants.ImConstants;
import org.qiyu.live.im.constants.ImMsgCodeEnum;
import org.qiyu.live.im.core.server.common.ChannelHandlerContextCache;
import org.qiyu.live.im.core.server.common.ImContextUtils;
import org.qiyu.live.im.core.server.common.ImMsg;
import org.qiyu.live.im.core.server.handler.SimplyHandler;
import org.qiyu.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.qiyu.live.im.dto.ImMsgBody;
import org.qiyu.live.im.interfaces.ImTokenRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登入消息处理器
 *
 * @Author: QingY
 * @Date: Created in 21:37 2024-04-27
 * @Description:
 */
@Component
public class LoginImMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginImMsgHandler.class);
    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        // 防止重复请求
        if (ImContextUtils.getUserId(ctx) != null) {
            return;
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            LOGGER.error("body error,imMsg is {}", imMsg);
            throw new IllegalArgumentException("body error");
        }
        ImMsgBody imMsgBody = JSON.parseObject(new String(body), ImMsgBody.class);
        Long userIdFromMsg = imMsgBody.getUserId();
        Integer appId = imMsgBody.getAppId();
        String token = imMsgBody.getToken();
        if (StringUtils.isEmpty(token) || userIdFromMsg < 10000 || appId < 10000) {
            ctx.close();
            LOGGER.error("param error,imMsg is {}", imMsg);
            throw new IllegalArgumentException("param error");
        }
        Long userId = imTokenRpc.getUserIdByToken(token);
        // token校验成功 而且和传递过来的userId是同一个，则允许建立连接
        if (userId != null && userId.equals(userIdFromMsg)) {
            // 按照userId保存好相关的channel对象信息
            ChannelHandlerContextCache.put(userId, ctx);
            ImContextUtils.setUserId(ctx, userId);
            // 将im消息回写给客户端
            ImMsgBody respBody = new ImMsgBody();
            respBody.setAppId(appId);
            respBody.setUserId(userId);
            respBody.setData("true");
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(respBody));
            stringRedisTemplate.opsForValue().set(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId,
                    ChannelHandlerContextCache.getServerIpAddress(),
                    ImConstants.DEFAULT_HEART_BEAT_GAP * 2, TimeUnit.SECONDS);
            LOGGER.info("[LoginMsgHandler] login success,userId is {},appId is {}", userId, appId);
            ctx.writeAndFlush(respMsg);
            return;
        }
        ctx.close();
        LOGGER.error("token check error,imMsg is {}", imMsg);
        throw new IllegalArgumentException("token check error");
    }
}
