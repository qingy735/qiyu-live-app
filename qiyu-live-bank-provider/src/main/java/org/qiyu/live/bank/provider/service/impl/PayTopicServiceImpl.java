package org.qiyu.live.bank.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.qiyu.live.bank.provider.dao.mapper.IPayTopicMapper;
import org.qiyu.live.bank.provider.dao.po.PayTopicPO;
import org.qiyu.live.bank.provider.service.IPayTopicService;
import org.qiyu.live.common.interfaces.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;

/**
 * @Author idea
 * @Date: Created in 22:09 2023/8/19
 * @Description
 */
@Service
public class PayTopicServiceImpl implements IPayTopicService {

    @Resource
    private IPayTopicMapper payTopicMapper;

    @Override
    public PayTopicPO getByCode(Integer code) {
        LambdaQueryWrapper<PayTopicPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayTopicPO::getBizCode, code);
        queryWrapper.eq(PayTopicPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return payTopicMapper.selectOne(queryWrapper);
    }
}
