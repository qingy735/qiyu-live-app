package org.qiyu.live.msg.provider.consumer.handler;

import org.qiyu.live.im.dto.ImMsgBody;

/**
 * @Author: QingY
 * @Date: Created in 16:57 2024-05-06
 * @Description:
 */
public interface MessageHandler {

    /**
     * 处理im服务投递过来的消息内容
     *
     * @param imMsgBody
     */
    void onMsgReceive(ImMsgBody imMsgBody);
}
