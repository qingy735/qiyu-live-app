package org.qiyu.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.bank.interfaces.dto.PayOrderDTO;
import org.qiyu.live.bank.interfaces.rpc.IPayOrderRpc;
import org.qiyu.live.bank.provider.dao.po.PayOrderPO;
import org.qiyu.live.bank.provider.service.IPayOrderService;
import org.qiyu.live.common.interfaces.utils.ConvertBeanUtils;

/**
 * @Author: QingY
 * @Date: Created in 19:17 2024-05-16
 * @Description:
 */
@DubboService
public class PayOrderRpcImpl implements IPayOrderRpc {
    @Resource
    private IPayOrderService payOrderService;

    @Override
    public String insertOne(PayOrderDTO payOrderDTO) {
        return payOrderService.insertOne(ConvertBeanUtils.convert(payOrderDTO, PayOrderPO.class));
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        return payOrderService.updateOrderStatus(id, status);
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        return payOrderService.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean payNotify(PayOrderDTO payOrderDTO) {
        return payOrderService.payNotify(payOrderDTO);
    }
}
