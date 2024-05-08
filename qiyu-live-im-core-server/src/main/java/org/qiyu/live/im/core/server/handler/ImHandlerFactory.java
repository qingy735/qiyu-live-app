package org.qiyu.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.qiyu.live.im.core.server.common.ImMsg;

/**
 * @Author: QingY
 * @Date: Created in 21:41 2024-04-27
 * @Description:
 */
public interface ImHandlerFactory {

    /**
     * 按照imMsg的code去筛选
     *
     * @param ctx
     * @param imMsg
     */
    void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg);
}
