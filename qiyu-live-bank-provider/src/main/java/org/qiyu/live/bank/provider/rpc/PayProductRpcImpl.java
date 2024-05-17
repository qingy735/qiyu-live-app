package org.qiyu.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.bank.interfaces.dto.PayProductDTO;
import org.qiyu.live.bank.interfaces.rpc.IPayProductRpc;
import org.qiyu.live.bank.provider.service.IPayProductService;

import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 17:01 2024-05-16
 * @Description:
 */
@DubboService
public class PayProductRpcImpl implements IPayProductRpc {

    @Resource
    private IPayProductService payProductService;

    @Override
    public List<PayProductDTO> products(Integer type) {
        return payProductService.products(type);
    }

    @Override
    public PayProductDTO getByProductId(Integer productId) {
        return payProductService.getByProductId(productId);
    }
}
