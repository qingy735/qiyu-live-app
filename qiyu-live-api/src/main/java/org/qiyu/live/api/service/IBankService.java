package org.qiyu.live.api.service;

import org.qiyu.live.api.vo.req.PayProductReqVO;
import org.qiyu.live.api.vo.resp.PayProductRespVO;
import org.qiyu.live.api.vo.resp.PayProductVO;
import org.qiyu.live.bank.interfaces.dto.PayProductDTO;

import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 17:55 2024-05-16
 * @Description:
 */
public interface IBankService {
    /**
     * 查询相关的产品列表信息
     *
     * @param type
     * @return
     */
    PayProductVO products(Integer type);

    /**
     * 发起支付
     *
     * @param payProductReqVO
     * @return
     */
    PayProductRespVO payProduct(PayProductReqVO payProductReqVO);
}
