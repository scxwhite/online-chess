package com.xynu.service;

import com.xynu.entity.ChessLog;
import com.xynu.entity.Scores;
import com.xynu.model.ScoresVO;

import java.util.List;

/**
 *
 * @author xiaosuda
 * @date 2018/4/11
 */
public interface ScoresService {

    List<ScoresVO> getTopScores();

    boolean updateScores(Scores scores);

    Scores findById(Integer id);

    boolean addLog(ChessLog chessLog);


    boolean updateStatus(Integer id, Integer type);

    Integer findRank(Integer id);
}
