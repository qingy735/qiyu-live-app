package org.qiyu.live.im.core.server.service.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.qiyu.live.im.constants.ImMsgCodeEnum;
import org.qiyu.live.im.core.server.common.ChannelHandlerContextCache;
import org.qiyu.live.im.core.server.common.ImMsg;
import org.qiyu.live.im.core.server.service.IMsgAckCheckService;
import org.qiyu.live.im.core.server.service.IRouterHandlerService;
import org.qiyu.live.im.dto.ImMsgBody;

import java.util.UUID;

/**
 * @Author: QingY
 * @Date: Created in 16:52 2024-05-06
 * @Description:
 */
public class RouterHandlerServiceImpl implements IRouterHandlerService {

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void onReceive(ImMsgBody imMsgBody) {
        if (sendMsgToClient(imMsgBody)) {
            // 当im服务器推送消息给了服务端 我们需要记录ack
            msgAckCheckService.recordMsgAck(imMsgBody, 1);
            msgAckCheckService.sendDelayMsg(imMsgBody);
        }
    }

    @Override
    public boolean sendMsgToClient(ImMsgBody imMsgBody) {
        long userId = imMsgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx != null) {
            String msgId = UUID.randomUUID().toString();
            imMsgBody.setMsgId(msgId);
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(imMsgBody));
            ctx.writeAndFlush(respMsg);
            return true;
        }
        return false;
    }
}
