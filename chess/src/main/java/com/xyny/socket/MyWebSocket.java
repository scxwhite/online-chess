package com.xyny.socket;

import lombok.NoArgsConstructor;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author suchengxiang
 * @version 创建时间：2018/3/19 21:50
 */
@ServerEndpoint("/push")
@NoArgsConstructor
public class MyWebSocket {


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("连接成功:" + session);
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        System.out.println("收到的信息:" + msg);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("关闭");
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

}
