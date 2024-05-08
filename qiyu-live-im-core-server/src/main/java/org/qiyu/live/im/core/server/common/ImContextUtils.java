package org.qiyu.live.im.core.server.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * 利用ChannelHandlerContext的attr方法去绑定/获取一些业务属性
 *
 * @Author: QingY
 * @Date: Created in 21:56 2024-04-29
 * @Description:
 */
public class ImContextUtils {


    public static void setUserId(ChannelHandlerContext ctx, Long userId) {
        ctx.attr(ImContextAttr.USER_ID).set(userId);
    }

    public static Integer getAppId(ChannelHandlerContext ctx) {
        return ctx.attr(ImContextAttr.APP_ID).get();
    }

    public static void setAppId(ChannelHandlerContext ctx, Integer appId) {
        ctx.attr(ImContextAttr.APP_ID).set(appId);
    }

    public static Long getUserId(ChannelHandlerContext ctx) {
        return ctx.attr(ImContextAttr.USER_ID).get();
    }

    public static void removeUserId(ChannelHandlerContext ctx) {
        ctx.attr(ImContextAttr.USER_ID).remove();
    }

    public static void removeAppId(ChannelHandlerContext ctx) {
        ctx.attr(ImContextAttr.APP_ID).remove();
    }

}
