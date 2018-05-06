package com.xynu.service;

import com.xynu.entity.User;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */
public interface UserService {

    Integer loginCheck(User user);

    User findById(Integer id);

    boolean addUser(User user);
}
