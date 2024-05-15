package org.qiyu.live.framework.web.starter.context;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.qiyu.live.framework.web.starter.error.QiyuErrorException;
import org.qiyu.live.framework.web.starter.limit.RequestLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author: QingY
 * @Date: Created in 15:28 2024-05-15
 * @Description:
 */
public class RequestLimitInterceptor implements HandlerInterceptor {

    @Value("${spring.application.name}")
    private String applicationName;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean isLimit = handlerMethod.getMethod().isAnnotationPresent(RequestLimit.class);
        // 如果当前方法有限流注解
        if (isLimit) {
            RequestLimit requestLimit = handlerMethod.getMethod().getAnnotation(RequestLimit.class);
            Long userId = QiyuRequestContext.getUserId();
            String cacheKey = applicationName + ":" + request.getRequestURI() + ":" + userId;
            int limit = requestLimit.limit();
            int second = requestLimit.second();
            Integer reqTime = (Integer) Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey)).orElse(0);
            if (reqTime == 0) {
                redisTemplate.opsForValue().set(cacheKey, 1, second, TimeUnit.SECONDS);
                return true;
            } else if (reqTime < limit) {
                redisTemplate.opsForValue().increment(cacheKey, 1);
                return true;
            }
            throw new QiyuErrorException(-10001, requestLimit.msg());
        }
        return true;
    }
}
