package org.qiyu.live.account.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.account.interfaces.IAccountTokenRpc;
import org.qiyu.live.account.provider.service.IAccountTokenService;

/**
 * @Author: QingY
 * @Date: Created in 16:06 2024-04-21
 * @Description:
 */
@DubboService
public class AccountTokenRpcImpl implements IAccountTokenRpc {

    @Resource
    private IAccountTokenService accountTokenService;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        return accountTokenService.createAndSaveLoginToken(userId);
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        return accountTokenService.getUserIdByToken(tokenKey);
    }
}
