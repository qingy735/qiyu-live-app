package org.qiyu.live.bank.interfaces.rpc;

import org.qiyu.live.bank.interfaces.dto.AccountTradeReqDTO;
import org.qiyu.live.bank.interfaces.dto.AccountTradeRespDTO;
import org.qiyu.live.bank.interfaces.dto.QiyuCurrencyAccountDTO;

/**
 * @Author: QingY
 * @Date: Created in 19:01 2024-05-15
 * @Description:
 */
public interface IQiyuCurrencyAccountRpc {
    /**
     * 增加虚拟币
     *
     * @param userId
     * @param num
     */
    void incr(long userId, int num);

    /**
     * 扣减虚拟币
     *
     * @param userId
     * @param num
     */
    void decr(long userId, int num);

    /**
     * 查询余额
     *
     * @param userId
     * @return
     */
    Integer getBalance(long userId);

    /**
     * 专门给送礼业务调用的扣减库存逻辑
     *
     * @param reqDTO
     * @return
     */
    AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO reqDTO);
}
