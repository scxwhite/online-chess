package com.xynu.service.impl;

import com.xynu.entity.ChessLog;
import com.xynu.entity.Scores;
import com.xynu.entity.User;
import com.xynu.mapper.ScoresMapper;
import com.xynu.model.ScoresVO;
import com.xynu.service.ScoresService;
import com.xynu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
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

    @Override
    public boolean updateScores(Scores scores) {
        Integer res = scoresMapper.updateById(scores);
        return res != null && res > 0;
    }

    @Override
    public Scores findById(Integer id) {
        return scoresMapper.selectById(id);
    }

    @Override
    public boolean addLog(ChessLog chessLog) {
        Integer integer = scoresMapper.insertLog(chessLog);
        return integer != null && integer > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Integer id, Integer type) {
        try {
            Scores userScore = this.findById(id);
            if (userScore == null) {
                userScore = Scores.builder().userId(id).failTimes(0).winTimes(0).build();
                scoresMapper.insert(userScore);
            }
            //分数
            if (type == 0) {
                userScore.setFailTimes(userScore.getFailTimes() + 1);
            } else {
                userScore.setWinTimes(userScore.getWinTimes() + 1);
            }
            this.updateScores(userScore);
            //比赛日志
            this.addLog(ChessLog.builder().userId(id).type(type).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Integer findRank(Integer id) {
        Scores scores = scoresMapper.selectById(id);
        Integer rank = scoresMapper.selectRank(scores.getWinTimes()) + 1;
        return rank;
    }
}
