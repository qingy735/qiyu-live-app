package org.qiyu.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qiyu.live.user.provider.dao.po.UserPO;

/**
 * @Author: QingY
 * @Date: Created in 21:47 2024-04-07
 * @Description:
 */
@Mapper
public interface IUserMapper extends BaseMapper<UserPO> {
}
