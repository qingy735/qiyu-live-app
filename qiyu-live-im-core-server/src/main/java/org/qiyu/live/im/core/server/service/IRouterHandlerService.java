package org.qiyu.live.im.core.server.service;

import org.qiyu.live.im.dto.ImMsgBody;

/**
 * @Author: QingY
 * @Date: Created in 16:47 2024-05-06
 * @Description:
 */
public interface IRouterHandlerService {

    void onReceive(ImMsgBody imMsgBody);

    /**
     * 发送消息给客户端
     * @param imMsgBody
     */
    boolean sendMsgToClient(ImMsgBody imMsgBody);

}
