package org.qiyu.live.bank.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.qiyu.live.bank.interfaces.dto.PayProductDTO;
import org.qiyu.live.bank.provider.dao.mapper.IPayProductMapper;
import org.qiyu.live.bank.provider.dao.po.PayProductPO;
import org.qiyu.live.bank.provider.service.IPayProductService;
import org.qiyu.live.common.interfaces.enums.CommonStatusEnum;
import org.qiyu.live.common.interfaces.utils.ConvertBeanUtils;
import org.qiyu.live.framework.redis.starter.key.BankProviderCacheKeyBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: QingY
 * @Date: Created in 16:47 2024-05-16
 * @Description:
 */
@Service
public class PayProductServiceImpl implements IPayProductService {

    @Resource
    private IPayProductMapper payProductMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private BankProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public List<PayProductDTO> products(Integer type) {
        String cacheKey = cacheKeyBuilder.buildPayProductCache(type);
        List<PayProductDTO> cacheList = redisTemplate.opsForList().range(cacheKey, 0, 30).stream().map(x -> {
            return (PayProductDTO) x;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cacheList)) {
            // 控制缓存
            if (cacheList.get(0).getId() == null) {
                return Collections.emptyList();
            }
            return cacheList;
        }
        LambdaQueryWrapper<PayProductPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayProductPO::getType, type);
        queryWrapper.eq(PayProductPO::getValidStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.orderByDesc(PayProductPO::getPrice);
        List<PayProductDTO> payProductDTOS = ConvertBeanUtils.convertList(payProductMapper.selectList(queryWrapper), PayProductDTO.class);
        if (CollectionUtils.isEmpty(payProductDTOS)) {
            redisTemplate.opsForList().leftPush(cacheKey, new PayProductDTO());
            redisTemplate.expire(cacheKey, 3, TimeUnit.MINUTES);
            return Collections.emptyList();
        }
        redisTemplate.opsForList().leftPushAll(cacheKey, payProductDTOS.toArray());
        redisTemplate.expire(cacheKey, 30, TimeUnit.MINUTES);
        return payProductDTOS;
    }

    @Override
    public PayProductDTO getByProductId(Integer productId) {
        String cacheKey = cacheKeyBuilder.buildPayProductItemCache(productId);
        PayProductDTO payProductDTO = (PayProductDTO) redisTemplate.opsForValue().get(cacheKey);
        if (payProductDTO != null) {
            if (payProductDTO.getId() == null) {
                return null;
            }
            return payProductDTO;
        }
        PayProductPO payProductPO = payProductMapper.selectById(productId);
        if (payProductPO != null) {
            PayProductDTO resultItem = ConvertBeanUtils.convert(payProductPO, PayProductDTO.class);
            redisTemplate.opsForValue().set(cacheKey, resultItem, 30, TimeUnit.MINUTES);
            return resultItem;
        }
        // 空值缓存
        redisTemplate.opsForValue().set(cacheKey, new PayProductDTO(), 5, TimeUnit.MINUTES);
        return null;
    }

}
