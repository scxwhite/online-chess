package com.xynu.model;

import lombok.Data;

/**
 * @author xiaosuda
 * @date 2018/4/11
 */
@Data
public class ScoresVO {

    private String username;
    private Integer winTimes;
    private Integer failTimes;
    private String userImage;
}
