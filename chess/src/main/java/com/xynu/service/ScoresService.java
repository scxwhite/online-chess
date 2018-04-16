package com.xynu.service;

import com.xynu.model.ScoresVO;

import java.util.List;

/**
 *
 * @author xiaosuda
 * @date 2018/4/11
 */
public interface ScoresService {

    List<ScoresVO> getTopScores();
}
