package org.qiyu.live.bank.provider.service;

import org.qiyu.live.bank.interfaces.dto.PayOrderDTO;
import org.qiyu.live.bank.provider.dao.po.PayOrderPO;

/**
 * @Author: QingY
 * @Date: Created in 19:17 2024-05-16
 * @Description:
 */
public interface IPayOrderService {

    /**
     * 根据订单id查询
     *
     * @param orderId
     */
    PayOrderPO queryByOrderId(String orderId);

    /**
     * 插入订单
     *
     * @param payOrderPO
     */
    String insertOne(PayOrderPO payOrderPO);


    /**
     * 根据主键id做更新
     *
     * @param id
     * @param status
     */
    boolean updateOrderStatus(Long id,Integer status);

    /**
     * 根据订单id做更新
     *
     * @param orderId
     * @param status
     */
    boolean updateOrderStatus(String orderId,Integer status);

    /**
     * 支付回调需要请求该接口
     *
     * @param payOrderDTO
     * @return
     */
    boolean payNotify(PayOrderDTO payOrderDTO);

}
