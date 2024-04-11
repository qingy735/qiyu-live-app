package org.qiyu.live.user.interfaces;

import org.qiyu.live.user.constants.UserTagsEnum;

/**
 * @Author: QingY
 * @Date: Created in 21:48 2024-04-10
 * @Description: 用户标签RPC服务
 */
public interface IUsersTagRpc {

    /**
     * 设置标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 取消标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 是否包含某个标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean containTag(Long userId, UserTagsEnum userTagsEnum);

}
