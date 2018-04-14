package com.xynu.model;

/**
 *
 * @author xiaosuda
 * @date 2018/3/20
 */
public enum  ChessStatus {
    PREPARE("prepare"),
    MOVE("move"),
    LEAVE("leave"),
    CHAT("chat"),
    CREATE_ROOM("createRoom"),
    ENTER_ROOM("enterRoom"),
    ROOM_START("roomStart"),
    LEAVE_ROOM("leaveRoom"),
    ERROE("error"),
    START("start");

    private String type;

    ChessStatus(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
