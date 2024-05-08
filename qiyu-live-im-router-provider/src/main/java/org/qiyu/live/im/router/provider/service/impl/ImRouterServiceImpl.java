package org.qiyu.live.im.router.provider.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.qiyu.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.qiyu.live.im.core.server.interfaces.rpc.IRouterHandlerRpc;
import org.qiyu.live.im.dto.ImMsgBody;
import org.qiyu.live.im.router.provider.service.ImRouterService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author idea
 * @Date: Created in 10:30 2023/7/12
 * @Description
 */
@Service
public class ImRouterServiceImpl implements ImRouterService {
    @DubboReference
    private IRouterHandlerRpc routerHandlerRpc;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean sendMsg(ImMsgBody imMsgBody) {
        String binAddress = stringRedisTemplate.opsForValue().get(ImCoreServerConstants.IM_BIND_IP_KEY + imMsgBody.getAppId() + ":" + imMsgBody.getUserId());
        if (StringUtils.isEmpty(binAddress)) {
            return false;
        }
        RpcContext.getContext().set("ip", binAddress);
        routerHandlerRpc.sendMsg(imMsgBody);
        return true;
    }
}
