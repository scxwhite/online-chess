package com.xynu.model;

import lombok.Data;

import javax.websocket.Session;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  开始比赛的两个session对象
 * @author xiaosuda
 * @date 2018/3/20
 */
@Data
public class RoomPlayer {
    private Session first;
    private Session second;
    private AtomicInteger peopleNum;
    private String roomName;
    private Integer roomId;
    private AtomicInteger prepareNum;

}
