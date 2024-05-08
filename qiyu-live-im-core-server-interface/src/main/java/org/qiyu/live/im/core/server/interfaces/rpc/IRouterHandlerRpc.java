package org.qiyu.live.im.core.server.interfaces.rpc;

import org.qiyu.live.im.dto.ImMsgBody;

/**
 * 专门给router层的服务进行调用的接口
 *
 * @Author idea
 * @Date: Created in 10:19 2023/7/12
 * @Description
 */
public interface IRouterHandlerRpc {

    /**
     * 按照用户id进行消息的发送
     *
     * @param imMsgBody
     */
    void sendMsg(ImMsgBody imMsgBody);
}
