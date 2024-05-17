package org.qiyu.live.bank.provider.service;

import org.qiyu.live.bank.interfaces.dto.AccountTradeReqDTO;
import org.qiyu.live.bank.interfaces.dto.AccountTradeRespDTO;
import org.qiyu.live.bank.interfaces.dto.QiyuCurrencyAccountDTO;

/**
 * @Author: QingY
 * @Date: Created in 18:52 2024-05-15
 * @Description:
 */
public interface IQiyuCurrencyAccountService {

    /**
     * 新增账户
     *
     * @param userId
     */
    boolean insertOne(long userId);

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
     * 查询账户
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


    /**
     * 底层需要判断用户余额是否充足，充足则扣减，不足则拦截
     *
     * @param reqDTO
     * @return
     */
    AccountTradeRespDTO consume(AccountTradeReqDTO reqDTO);
}
