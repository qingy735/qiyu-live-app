package org.qiyu.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.qiyu.live.user.provider.service.IUserService;
import org.qiyu.live.common.interfaces.utils.ConvertBeanUtils;
import org.qiyu.live.user.dto.UserDTO;
import org.qiyu.live.user.provider.dao.mapper.IUserMapper;
import org.qiyu.live.user.provider.dao.po.UserPO;
import org.springframework.stereotype.Service;

/**
 * @Author: QingY
 * @Date: Created in 21:50 2024-04-07
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Override
    public UserDTO getById(Long userId) {
        if (userId == null) {
            return null;
        }
        return ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        try {
            userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        try {
            userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
