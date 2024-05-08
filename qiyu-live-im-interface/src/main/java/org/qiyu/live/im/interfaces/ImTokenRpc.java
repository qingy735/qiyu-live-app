package org.qiyu.live.im.interfaces;

/**
 * @Author: QingY
 * @Date: Created in 21:29 2024-04-28
 * @Description:
 */
public interface ImTokenRpc {

    /**
     * 创建用户登录im服务的token
     *
     * @param userId
     * @param appId
     * @return
     */
    String createImLoginToken(long userId, int appId);

    /**
     * 根据token检索用户id
     *
     * @param token
     * @return
     */
    Long getUserIdByToken(String token);

}
