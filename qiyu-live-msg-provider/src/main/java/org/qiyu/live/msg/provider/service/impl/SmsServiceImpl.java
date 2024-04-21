package org.qiyu.live.msg.provider.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.qiyu.live.framework.redis.starter.key.MsgProviderCacheKeyBuilder;
import org.qiyu.live.msg.dto.MsgCheckDTO;
import org.qiyu.live.msg.enums.MsgSendResultEnum;
import org.qiyu.live.msg.provider.config.ThreadPoolManager;
import org.qiyu.live.msg.provider.dao.mapper.SmsMapper;
import org.qiyu.live.msg.provider.dao.po.SmsPO;
import org.qiyu.live.msg.provider.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: QingY
 * @Date: Created in 18:55 2024-04-19
 * @Description:
 */
@Service
public class SmsServiceImpl implements ISmsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Resource
    private SmsMapper smsMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MsgProviderCacheKeyBuilder msgProviderCacheKeyBuilder;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return MsgSendResultEnum.MSG_PARAM_ERROR;
        }
        // 生成验证码 4位 8位 有效期 同一个手机号不能重发
        String codeCacheKey = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        if (redisTemplate.hasKey(codeCacheKey)) {
            LOGGER.warn("该手机号短信发送过于频繁，phone is {}", phone);
            return MsgSendResultEnum.SEND_FAIL;
        }
        int code = RandomUtils.nextInt(100000, 999999);
        redisTemplate.opsForValue().set(codeCacheKey, code, 1, TimeUnit.DAYS);
        // 发送验证码
        ThreadPoolManager.commonAsyncPool.execute(() -> {
            boolean sendStatus = mockSendSms(phone, code);
            // 插入验证码发送记录
            if (sendStatus) {
                insertOne(phone, code);
            }
        });
        return MsgSendResultEnum.SEND_SUCCESS;
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        // 参数校验
        if (StringUtils.isEmpty(phone) || code == null || code < 100000 || code > 999999) {
            return new MsgCheckDTO(false, "参数异常");
        }
        // redis校验验证码
        String codeCacheKey = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        Integer cacheCode = (Integer) redisTemplate.opsForValue().get(codeCacheKey);
        if (cacheCode == null || cacheCode < 100000 || cacheCode > 999999) {
            return new MsgCheckDTO(false, "验证码已过期");
        }
        if (cacheCode.equals(code)) {
            // redisTemplate.delete(codeCacheKey);
            return new MsgCheckDTO(true, "验证码校验成功");
        }
        return new MsgCheckDTO(false, "验证码校验失败");
    }

    @Override
    public void insertOne(String phone, Integer code) {
        SmsPO smsPO = new SmsPO();
        smsPO.setPhone(phone);
        smsPO.setCode(code);
        smsMapper.insert(smsPO);
    }

    /**
     * 模拟发送短信过程
     *
     * @param phone
     * @param code
     * @return
     */
    private boolean mockSendSms(String phone, Integer code) {
        try {
            LOGGER.info("================= 创建短信发送通道中 =================, phone is {}, code is {}", phone, code);
            Thread.sleep(1000);
            LOGGER.info("================= 短信已经发送成功 =================");
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
