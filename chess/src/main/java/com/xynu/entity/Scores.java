package com.xynu.entity;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author xiaosuda
 * @date 2018/4/10
 */
@Data
public class Scores {
    /**
     * 用户ID 主键ID
     */
    private Integer userId;
    /**
     * 胜利次数
     */
    private Integer winTimes;
    /**
     * 失败次数
     */
    private Integer failTimes;
}
