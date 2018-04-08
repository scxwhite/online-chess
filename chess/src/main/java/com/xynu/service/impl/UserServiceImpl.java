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
    public boolean loginCheck(User user) {
        Integer x = userMapper.selectByUser(user);
        if (x == null || x == 0) {
            return false;
        }
        return true;
    }
}
