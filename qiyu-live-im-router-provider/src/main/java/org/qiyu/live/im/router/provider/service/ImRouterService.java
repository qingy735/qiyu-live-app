package org.qiyu.live.im.router.provider.service;

import org.qiyu.live.im.dto.ImMsgBody;

/**
 * @Author idea
 * @Date: Created in 10:30 2023/7/12
 * @Description
 */
public interface ImRouterService {


    /**
     * 发送消息
     *
     * @param imMsgBody
     * @return
     */
    boolean sendMsg(ImMsgBody imMsgBody);
}
