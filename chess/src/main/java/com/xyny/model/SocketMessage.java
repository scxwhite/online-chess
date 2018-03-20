package com.xyny.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {
    /**
     * 消息内容
     */
    private String content;
    /**
     *  请求类型
     */
    private String type;
}
