package org.qiyu.live.bank.interfaces.rpc;

import org.qiyu.live.bank.interfaces.dto.PayOrderDTO;

/**
 * @Author: QingY
 * @Date: Created in 19:18 2024-05-16
 * @Description:
 */
public interface IPayOrderRpc {

    /**
     * 插入订单
     *
     * @param payOrderDTO
     */
    String insertOne(PayOrderDTO payOrderDTO);


    /**
     * 根据主键id做更新
     *
     * @param id
     * @param status
     */
    boolean updateOrderStatus(Long id, Integer status);

    /**
     * 根据订单id做更新
     *
     * @param orderId
     * @param status
     */
    boolean updateOrderStatus(String orderId, Integer status);

    /**
     * 支付回调需要请求该接口
     *
     * @param payOrderDTO
     * @return
     */
    boolean payNotify(PayOrderDTO payOrderDTO);
}
