package org.qiyu.live.user.provider.service;

import org.qiyu.live.user.dto.UserLoginDTO;
import org.qiyu.live.user.dto.UserPhoneDTO;

import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 20:45 2024-04-19
 * @Description:
 */
public interface IUserPhoneService {

    /**
     * 登录+注册初始化
     *
     * @param phone
     * @return
     */
    UserLoginDTO login(String phone);

    /**
     * 根据手机号信息查询相关用户信息
     *
     * @param phone
     * @return
     */
    UserPhoneDTO queryByPhone(String phone);

    /**
     * 根据userId查询信息
     *
     * @param userId
     * @return
     */
    List<UserPhoneDTO> queryByUserId(Long userId);

}
