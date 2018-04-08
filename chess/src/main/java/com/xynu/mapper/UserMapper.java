package com.xynu.mapper;

import com.xynu.entity.User;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */
public interface UserMapper extends BaseMapper<User>{
    /**
     * 检测用户是否存在
     * @param user 用户信息
     * @return  结果
     */
    Integer selectByUser(User user);
}
