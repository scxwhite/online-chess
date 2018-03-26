package com.xyny.service.impl;

import com.xyny.entity.User;
import com.xyny.service.UserService;
import org.springframework.stereotype.Service;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */

@Service
public class UserServiceImpl implements UserService {

    @Override
    public boolean loginCheck(User user) {
        return true;
    }
}
