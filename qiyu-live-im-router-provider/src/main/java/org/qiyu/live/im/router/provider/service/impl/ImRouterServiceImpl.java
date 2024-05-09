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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        binAddress = binAddress.substring(binAddress.indexOf("%"));
        RpcContext.getContext().set("ip", binAddress);
        routerHandlerRpc.sendMsg(imMsgBody);
        return true;
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        List<Long> userIdList = imMsgBodyList.stream().map(ImMsgBody::getUserId).collect(Collectors.toList());
        // 根据userId 将不同的userId的ImMsgBody分类存入map
        Map<Long, ImMsgBody> userIdMsgMap = imMsgBodyList.stream().collect(Collectors.toMap(ImMsgBody::getUserId, x -> x));
        // 保证整个list集合的appId统一
        Integer appId = imMsgBodyList.get(0).getAppId();
        List<String> cacheKeyList = new ArrayList<>();
        userIdList.forEach(userId -> {
            String cacheKey = ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId;
            cacheKeyList.add(cacheKey);
        });
        // 批量取出每个用户绑定的ip地址
        List<String> ipList = stringRedisTemplate.opsForValue().multiGet(cacheKeyList);
        Map<String, List<Long>> userIdMap = new HashMap<>();
        ipList.forEach(ip -> {
            String currentIp = ip.substring(ip.indexOf("%"));
            Long userId = Long.valueOf(ip.substring(ip.indexOf("%"), -1));
            List<Long> currentUserIdList = userIdMap.get(currentIp);
            if (currentUserIdList == null) {
                currentUserIdList = new ArrayList<>();
            }
            currentUserIdList.add(userId);
            userIdMap.put(currentIp, currentUserIdList);
        });
        // 将连接同一台ip地址的imMsgBody组装到同一个list集合中，然后进行统一的发送
        for (String currentIp : userIdMap.keySet()) {
            RpcContext.getContext().set("ip", currentIp);
            List<ImMsgBody> batchsendMsgGroupByIpList = new ArrayList<>();
            List<Long> ipBindUserIdList = userIdMap.get(currentIp);
            for (Long userId : ipBindUserIdList) {
                ImMsgBody imMsgBody = userIdMsgMap.get(userId);
                batchsendMsgGroupByIpList.add(imMsgBody);
            }
            routerHandlerRpc.batchSendMsg(batchsendMsgGroupByIpList);
        }
    }
}
