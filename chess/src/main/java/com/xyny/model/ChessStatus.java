package com.xyny.model;

/**
 *
 * @author xiaosuda
 * @date 2018/3/20
 */
public enum  ChessStatus {
    PREPARE("prepare"),
    MOVE("move"),
    START("start");

    private String type;

    ChessStatus(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
