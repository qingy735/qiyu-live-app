package org.qiyu.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.bank.interfaces.dto.AccountTradeReqDTO;
import org.qiyu.live.bank.interfaces.dto.AccountTradeRespDTO;
import org.qiyu.live.bank.interfaces.dto.QiyuCurrencyAccountDTO;
import org.qiyu.live.bank.interfaces.rpc.IQiyuCurrencyAccountRpc;
import org.qiyu.live.bank.provider.service.IQiyuCurrencyAccountService;

/**
 * @Author: QingY
 * @Date: Created in 19:02 2024-05-15
 * @Description:
 */
@DubboService
public class QiyuCurrencyAccountRpcImpl implements IQiyuCurrencyAccountRpc {

    @Resource
    private IQiyuCurrencyAccountService qiyuCurrencyAccountService;

    @Override
    public void incr(long userId, int num) {
        qiyuCurrencyAccountService.incr(userId, num);
    }

    @Override
    public void decr(long userId, int num) {
        qiyuCurrencyAccountService.decr(userId, num);
    }

    @Override
    public Integer getBalance(long userId) {
        return qiyuCurrencyAccountService.getBalance(userId);
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO reqDTO) {
        return qiyuCurrencyAccountService.consumeForSendGift(reqDTO);
    }
}
