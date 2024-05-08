package org.qiyu.live.im.router.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.im.router.interfaces.rpc.ImOnlineRpc;
import org.qiyu.live.im.router.provider.service.ImOnlineService;

/**
 * @Author: QingY
 * @Date: Created in 14:26 2024-05-08
 * @Description:
 */
@DubboService
public class ImOnlineRpcImpl implements ImOnlineRpc {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineService.isOnline(userId, appId);
    }
}
