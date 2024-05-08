package org.qiyu.live.im.core.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 处理消息的编码过程
 *
 * @Author: QingY
 * @Date: Created in 21:19 2024-04-27
 * @Description:
 */
public class ImMsgEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        ImMsg imMsg = (ImMsg) o;
        byteBuf.writeShort(imMsg.getMagic());
        byteBuf.writeInt(imMsg.getCode());
        byteBuf.writeInt(imMsg.getLen());
        byteBuf.writeBytes(imMsg.getBody());
    }
}
