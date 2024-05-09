package org.qiyu.live.im.core.server.handler.ws;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.qiyu.live.im.constants.ImConstants;
import org.qiyu.live.im.constants.ImMsgCodeEnum;
import org.qiyu.live.im.core.server.common.ImMsg;
import org.qiyu.live.im.core.server.handler.impl.LoginImMsgHandler;
import org.qiyu.live.im.dto.ImMsgBody;
import org.qiyu.live.im.interfaces.ImTokenRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ws的握手连接处理器
 *
 * @Author idea
 * @Date created in 9:30 下午 2022/12/22
 */
@Component
@ChannelHandler.Sharable
public class WsSharkHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsSharkHandler.class);

    // 指定监听的端口
    @Value("${qiyu.im.ws.port}")
    private int port;
    @Value("${spring.cloud.nacos.discovery.ip}")
    private String serverIp;
    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private LoginImMsgHandler loginMsgHandler;

    private WebSocketServerHandshaker webSocketServerHandshaker;
    private static Logger logger = LoggerFactory.getLogger(WsSharkHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 握手接入ws
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
            return;
        }

        // 正常关闭链路
        if (msg instanceof CloseWebSocketFrame) {
            webSocketServerHandshaker.close(ctx.channel(), (CloseWebSocketFrame) ((WebSocketFrame) msg).retain());
            return;
        }
        // 将消息传递给下一个链路处理器去处理
        ctx.fireChannelRead(msg);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String webSocketUrl = "ws://" + serverIp + ":" + port;
        // 构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);
        String uri = msg.uri();
        String token = uri.substring(uri.indexOf("token"), uri.indexOf("&")).replaceAll("token=", "");
        Long userId = Long.valueOf(uri.substring(uri.indexOf("userId")).replaceAll("userId=", ""));
        Long queryUserId = imTokenRpc.getUserIdByToken(token);
        // token的尾部就是appId
        Integer appId = Integer.valueOf(token.substring(token.lastIndexOf("%") + 1));
        if (queryUserId == null || !queryUserId.equals(userId)) {
            LOGGER.error("[WsSharkHandler] token 校验不通过！");
            // 校验不通过，不允许建立连接
            ctx.close();
            return;
        }
        // 建立ws的握手连接
        webSocketServerHandshaker = wsFactory.newHandshaker(msg);

        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }

        ChannelFuture channelFuture = webSocketServerHandshaker.handshake(ctx.channel(), msg);
        // 首次握手建立ws连接后，返回一定的内容给到客户端
        if (channelFuture.isSuccess()) {
            loginMsgHandler.loginSuccessHandler(ctx, userId, appId);
            logger.info("[WebsocketSharkHandler] channel is connect!");
        }
    }
}
