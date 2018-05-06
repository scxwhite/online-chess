package com.xynu.service.impl;

import com.xynu.entity.Scores;
import com.xynu.entity.User;
import com.xynu.mapper.ScoresMapper;
import com.xynu.mapper.UserMapper;
import com.xynu.service.UserService;
import com.xynu.util.MD5Util;
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
    public boolean addUser(User user) {
        user.setPassword(MD5Util.getMD5(user.getPassword()));
        Integer insert = userMapper.insert(user);
        System.out.println("id=" + insert + "userId=" + user.getId());
        Scores scores = new Scores();
        scores.setFailTimes(0);
        scores.setWinTimes(0);
        scores.setUserId(user.getId());
        scoresMapper.insert(scores);
        return insert != null &&  insert > 0;
    }
}
