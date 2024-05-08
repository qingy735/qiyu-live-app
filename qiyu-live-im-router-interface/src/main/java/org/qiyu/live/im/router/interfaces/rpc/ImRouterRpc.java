package org.qiyu.live.im.router.interfaces.rpc;

import org.qiyu.live.im.dto.ImMsgBody;

/**
 * @Author idea
 * @Date: Created in 16:21 2023/7/11
 * @Description
 */
public interface ImRouterRpc {


    /**
     * 发送消息
     *
     * @param imMsgBody
     * @return
     */
    boolean sendMsg(ImMsgBody imMsgBody);
}
