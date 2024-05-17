package org.qiyu.live.bank.provider.service;

import org.qiyu.live.bank.interfaces.dto.PayProductDTO;

import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 16:46 2024-05-16
 * @Description:
 */
public interface IPayProductService {

    /**
     * 返回批量的商品信息
     *
     * @param type 不同的业务场景所使用的产品
     * @return
     */
    List<PayProductDTO> products(Integer type);

    /**
     * 根据产品id查询
     *
     * @param productId
     * @return
     */
    PayProductDTO getByProductId(Integer productId);
}
