package com.xynu.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author xiaosuda
 * @date 2018/4/10
 */
@Data
@Builder
public class ChessLog {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 创建时间（比赛时间）
     */
    private Date createTime;
    /**
     * 比赛结果（0：失败  1： 胜利）
     */
    private Integer type;
}
