package com.xynu.service.impl;

import com.xynu.entity.Scores;
import com.xynu.entity.User;
import com.xynu.mapper.ScoresMapper;
import com.xynu.model.ScoresVO;
import com.xynu.service.ScoresService;
import com.xynu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xiaosuda
 * @date 2018/4/11
 */
@Service
public class ScoresServiceImpl implements ScoresService {

    @Autowired
    private ScoresMapper scoresMapper;
    @Autowired
    private UserService userService;

    @Override
    public List<ScoresVO> getTopScores() {
        List<Scores> scores = scoresMapper.selectTopScores();
        List<ScoresVO> scoresVOS = new ArrayList<>(scores.size());
        scores.forEach(s -> {
            ScoresVO scoresVO = new ScoresVO();
            BeanUtils.copyProperties(s, scoresVO);
            User user = userService.findById(s.getUserId());
            scoresVO.setUserImage(user.getImageUrl());
            scoresVO.setUsername(user.getUsername());
            scoresVOS.add(scoresVO);
        });
        return scoresVOS;
    }
}
