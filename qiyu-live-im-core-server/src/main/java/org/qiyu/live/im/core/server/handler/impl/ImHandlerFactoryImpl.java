package org.qiyu.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.qiyu.live.im.core.server.common.ImMsg;
import org.qiyu.live.im.core.server.handler.ImHandlerFactory;
import org.qiyu.live.im.core.server.handler.SimplyHandler;
import org.qiyu.live.im.constants.ImMsgCodeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: QingY
 * @Date: Created in 21:42 2024-04-27
 * @Description:
 */
@Component
public class ImHandlerFactoryImpl implements ImHandlerFactory, InitializingBean {

    private static final Map<Integer, SimplyHandler> simplyHandlerMap = new HashMap<>();
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg) {
        SimplyHandler simplyHandler = simplyHandlerMap.get(imMsg.getCode());
        if (simplyHandler == null) {
            throw new IllegalArgumentException("msg code is error, code is :" + imMsg.getCode());
        }
        simplyHandler.handler(ctx, imMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("applicationContext" + applicationContext);
        // 登录消息包 登录token认证 channel和userId关联
        simplyHandlerMap.put(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), applicationContext.getBean(LoginImMsgHandler.class));
        // 登出消息包 正常断开im连接时发送
        simplyHandlerMap.put(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), applicationContext.getBean(LogoutImMsgHandler.class));
        // 业务消息包 im发送数据 接收数据
        simplyHandlerMap.put(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), applicationContext.getBean(BizImMsgHandler.class));
        // 心跳消息包 定时给im发送
        simplyHandlerMap.put(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), applicationContext.getBean(HeartBeatImHandler.class));
    }
}
