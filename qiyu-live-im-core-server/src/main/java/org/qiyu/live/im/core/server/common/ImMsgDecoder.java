package org.qiyu.live.im.core.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.qiyu.live.im.constants.ImConstants;

import java.util.List;

/**
 * 消息解码器
 *
 * @Author: QingY
 * @Date: Created in 21:19 2024-04-27
 * @Description:
 */
public class ImMsgDecoder extends ByteToMessageDecoder {

    private final int BASE_LENGTH = 2 + 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // byteBuf内容校验
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            if (byteBuf.readShort() != ImConstants.DEFAULT_MAGIC) {
                channelHandlerContext.close();
                return;
            }
            int code = byteBuf.readInt();
            int len = byteBuf.readInt();
            // 确保byteBuf剩余长度足够
            if (byteBuf.readableBytes() < len) {
                channelHandlerContext.close();
                return;
            }
            // byteBuf转换为ImMsg对象
            byte[] bytes = new byte[len];
            byteBuf.readBytes(bytes);
            ImMsg imMsg = new ImMsg();
            imMsg.setBody(bytes);
            imMsg.setLen(len);
            imMsg.setCode(code);
            list.add(imMsg);
        }
    }
}
