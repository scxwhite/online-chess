package com.xynu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * @author xiaosuda
 * @date 2018/4/10
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
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
