package org.qiyu.live.im.router.interfaces.rpc;

/**
 * 判断用户是否在线
 *
 * @Author: QingY
 * @Date: Created in 14:26 2024-05-08
 * @Description:
 */
public interface ImOnlineRpc {

    /**
     * 判断用户是否在线
     *
     * @param userId
     * @param appId
     * @return
     */
    boolean isOnline(long userId, int appId);

}
