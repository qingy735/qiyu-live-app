package org.qiyu.live.api.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.qiyu.live.api.service.IBankService;
import org.qiyu.live.api.vo.req.PayProductReqVO;
import org.qiyu.live.api.vo.resp.PayProductItemVO;
import org.qiyu.live.api.vo.resp.PayProductRespVO;
import org.qiyu.live.api.vo.resp.PayProductVO;
import org.qiyu.live.bank.interfaces.constants.OrderStatusEnum;
import org.qiyu.live.bank.interfaces.constants.PaySourceEnum;
import org.qiyu.live.bank.interfaces.dto.PayOrderDTO;
import org.qiyu.live.bank.interfaces.dto.PayProductDTO;
import org.qiyu.live.bank.interfaces.rpc.IPayOrderRpc;
import org.qiyu.live.bank.interfaces.rpc.IPayProductRpc;
import org.qiyu.live.bank.interfaces.rpc.IQiyuCurrencyAccountRpc;
import org.qiyu.live.framework.web.starter.context.QiyuRequestContext;
import org.qiyu.live.framework.web.starter.error.BizBaseErrorEnum;
import org.qiyu.live.framework.web.starter.error.ErrorAssert;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 17:55 2024-05-16
 * @Description:
 */
@Service
public class BankServiceImpl implements IBankService {
    @DubboReference
    private IPayProductRpc payProductRpc;
    @DubboReference
    private IPayOrderRpc payOrderRpc;
    @DubboReference
    private IQiyuCurrencyAccountRpc qiyuCurrencyAccountRpc;

    @Override
    public PayProductVO products(Integer type) {
        List<PayProductDTO> payProductDTOS = payProductRpc.products(type);
        PayProductVO payProductVO = new PayProductVO();
        List<PayProductItemVO> itemList = new ArrayList<>();
        for (PayProductDTO payProductDTO : payProductDTOS) {
            PayProductItemVO itemVO = new PayProductItemVO();
            itemVO.setName(payProductDTO.getName());
            itemVO.setId(payProductDTO.getId());
            itemVO.setCoinNum(JSON.parseObject(payProductDTO.getExtra()).getInteger("coin"));
            itemList.add(itemVO);
        }
        payProductVO.setPayProductItemVOList(itemList);
        payProductVO.setCurrentBalance(qiyuCurrencyAccountRpc.getBalance(QiyuRequestContext.getUserId()));
        return payProductVO;
    }

    @Override
    public PayProductRespVO payProduct(PayProductReqVO payProductReqVO) {
        // 参数校验
        ErrorAssert.isTrue(payProductReqVO != null && payProductReqVO.getProductId() != null && payProductReqVO.getPaySource() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isNotNull(PaySourceEnum.find(payProductReqVO.getPaySource()), BizBaseErrorEnum.PARAM_ERROR);
        PayProductDTO productDTO = payProductRpc.getByProductId(payProductReqVO.getProductId());
        ErrorAssert.isNotNull(productDTO, BizBaseErrorEnum.PARAM_ERROR);
        // 插入订单 待支付状态
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setUserId(QiyuRequestContext.getUserId());
        payOrderDTO.setProductId(payProductReqVO.getProductId());
        payOrderDTO.setSource(payProductReqVO.getPaySource());
        payOrderDTO.setPayChannel(payProductReqVO.getPayChannel());
        String orderId = payOrderRpc.insertOne(payOrderDTO);
        // 更新订单为支付中状态
        payOrderRpc.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        PayProductRespVO payProductRespVO = new PayProductRespVO();
        payProductRespVO.setOrderId(orderId);
        return payProductRespVO;
    }
}
