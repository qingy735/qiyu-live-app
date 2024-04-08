package org.qiyu.live.user.provider.service;

import org.apache.catalina.User;
import org.qiyu.live.user.dto.UserDTO;

public interface IUserService {

    /**
     * 根据用户Id进行查询
     *
     * @param userId
     * @return
     */
    UserDTO getById(Long userId);

    /**
     * 用户信息更新
     *
     * @param userDTO
     * @return
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入一条用户信息
     *
     * @param userDTO
     * @return
     */
    boolean insertOne(UserDTO userDTO);
}
