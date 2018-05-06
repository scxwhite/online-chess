package com.xynu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author xiaosuda
 * @date 2018/3/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
    /**
     * 消息内容
     */
    private String content;
    /**
     *  请求类型
     */
    private String type;
    /**
     * 其它信息
     */
    private Object extra;

    public SocketMessage(String type) {
        this.type = type;
    }

    public SocketMessage(String type, String content) {
        this.content = content;
        this.type = type;
    }
}