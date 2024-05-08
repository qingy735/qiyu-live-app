package org.qiyu.live.msg.provider.consumer.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.qiyu.live.im.constants.AppIdEnum;
import org.qiyu.live.im.constants.ImMsgCodeEnum;
import org.qiyu.live.im.dto.ImMsgBody;
import org.qiyu.live.im.router.interfaces.rpc.ImRouterRpc;
import org.qiyu.live.msg.dto.MessageDTO;
import org.qiyu.live.msg.enums.ImMsgBizCodeEnum;
import org.qiyu.live.msg.provider.consumer.handler.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * @Author: QingY
 * @Date: Created in 16:58 2024-05-06
 * @Description:
 */
@Component
public class SingleMessageHandlerImpl implements MessageHandler {

    @DubboReference
    private ImRouterRpc routerRpc;

    @Override
    public void onMsgReceive(ImMsgBody imMsgBody) {
        int bizCode = imMsgBody.getBizCode();
        // 直播间聊天消息
        if (ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode() == bizCode) {
            MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
            ImMsgBody respMsg = new ImMsgBody();
            respMsg.setUserId(messageDTO.getObjectId());
            respMsg.setAppId(AppIdEnum.QIYU_LIVE_BIZ.getCode());
            respMsg.setBizCode(ImMsgCodeEnum.IM_LOGIN_MSG.getCode());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", messageDTO.getUserId());
            jsonObject.put("content", messageDTO.getContent());
            respMsg.setData(jsonObject.toJSONString());
            routerRpc.sendMsg(respMsg);
        }
    }
}
