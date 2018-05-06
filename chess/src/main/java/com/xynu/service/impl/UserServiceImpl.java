package com.xynu.service.impl;

import com.xynu.entity.Scores;
import com.xynu.entity.User;
import com.xynu.mapper.ScoresMapper;
import com.xynu.mapper.UserMapper;
import com.xynu.service.UserService;
import com.xynu.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoresMapper scoresMapper;

    @Override
    public Integer loginCheck(User user) {
        return userMapper.selectByUser(user);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(User user) {
        Integer insert = null;
        try {
            user.setPassword(MD5Util.getMD5(user.getPassword()));
            insert = userMapper.insert(user);
            System.out.println("id=" + insert + "userId=" + user.getId());
            scoresMapper.insert(Scores.builder().userId(user.getId()).winTimes(0).failTimes(0).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return insert != null &&  insert > 0;
    }
}
