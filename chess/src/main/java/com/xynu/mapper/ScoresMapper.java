package com.xynu.mapper;

import com.xynu.entity.Scores;

import java.util.List;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */
public interface ScoresMapper extends BaseMapper<Scores>{

    List<Scores> selectTopScores();
}
