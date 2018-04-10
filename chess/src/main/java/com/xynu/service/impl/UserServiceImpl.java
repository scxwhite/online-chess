package com.xynu.service.impl;

import com.xynu.entity.User;
import com.xynu.mapper.UserMapper;
import com.xynu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer loginCheck(User user) {
        return userMapper.selectByUser(user);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

}
