package com.xyny.model;

import lombok.Builder;
import lombok.Data;

import javax.websocket.Session;

/**
 *  开始比赛的两个session对象
 * @author xiaosuda
 * @date 2018/3/20
 */
@Data
@Builder
public class ChessPlayer {
    private Session first;
    private Session second;

    /**
     *  一个用户发送给另外一个用户
     * @param people
     */
    public void sendToAnother(Session people) {

    }
}
