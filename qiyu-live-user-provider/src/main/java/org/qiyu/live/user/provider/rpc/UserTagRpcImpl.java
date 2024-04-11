package org.qiyu.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.user.constants.UserTagsEnum;
import org.qiyu.live.user.interfaces.IUsersTagRpc;
import org.qiyu.live.user.provider.service.IUserTagService;

/**
 * @Author: QingY
 * @Date: Created in 21:52 2024-04-10
 * @Description: 用户标签RPC服务实现类
 */
@DubboService
public class UserTagRpcImpl implements IUsersTagRpc {

    @Resource
    private IUserTagService userTagsService;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }
}
