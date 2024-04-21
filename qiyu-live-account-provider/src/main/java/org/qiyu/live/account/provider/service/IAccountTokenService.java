package org.qiyu.live.account.provider.service;

/**
 * @Author: QingY
 * @Date: Created in 16:07 2024-04-21
 * @Description:
 */
public interface IAccountTokenService {
    /**
     * 创建一个登录token
     *
     * @param userId
     * @return
     */
    String createAndSaveLoginToken(Long userId);

    /**
     * 校验用户token
     *
     * @param tokenKey
     * @return
     */
    Long getUserIdByToken(String tokenKey);
}
