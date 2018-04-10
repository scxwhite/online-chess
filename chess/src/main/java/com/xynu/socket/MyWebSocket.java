package com.xynu.socket;

import com.alibaba.fastjson.JSONObject;
import com.xynu.model.ChessStatus;
import com.xynu.model.SocketMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * @author xiaosuda
 * @date 2018/3/16
 */
@Slf4j
@Data
@Component
@ServerEndpoint("/websocket")
@NoArgsConstructor
public class MyWebSocket {

    private static Map<Session, MyWebSocket> sessionMap   = new ConcurrentHashMap<>();
    private static Map<Session, Session>     isPlayingMap = new ConcurrentHashMap<>();
    private static BlockingQueue<Session>    chessQueue   = new LinkedBlockingDeque<>();
    private Session session;
    private String userId;


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        List<String> id = parameterMap.get("id");
        if (id != null && id.size() == 1) {
            this.userId = id.get(0);
        }
        sessionMap.put(session, this);
        log.info("连接成功,当前在线人数:{}人", sessionMap.size());
    }

    /**
     * websocket 消息回调接口
     *
     * @param msg     消息  格式为：type;content
     * @param session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        SocketMessage socketMessage;
        try {
            socketMessage = JSONObject.parseObject(msg, SocketMessage.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (StringUtils.equals(socketMessage.getType(), ChessStatus.PREPARE.getType())) {
            //准备状态 想找队友
            if (chessQueue.size() == 0) {
                chessQueue.add(session);
                log.info("进入队列等待对手~~");
            } else if (chessQueue.contains(session)) {
                log.info("已经在等待中。。。");
            } else {
                try {
                    Session otherSession = chessQueue.take();
                    isPlayingMap.put(session, otherSession);
                    isPlayingMap.put(otherSession, session);
                    MyWebSocket mySelf = sessionMap.get(session);
                    MyWebSocket others = sessionMap.get(otherSession);
                    SocketMessage first = SocketMessage
                            .builder()
                            .type(ChessStatus.START.getType())
                            .build();
                    SocketMessage second = SocketMessage
                            .builder()
                            .type(ChessStatus.START.getType())
                            .build();
                    //根据当前时间判断谁先手
                    if (System.currentTimeMillis() % 2 == 0) {
                        first.setContent("you first");
                        first.setExtra(others.getUserId());
                        second.setExtra(mySelf.getUserId());
                    } else {
                        second.setContent("you first");
                        first.setExtra(others.getUserId());
                        second.setExtra(mySelf.getUserId());
                    }
                    mySelf.sendMessage(first);
                    others.sendMessage(second);
                    log.info("找到对手~~");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (StringUtils.equals(socketMessage.getType(), ChessStatus.MOVE.getType())) {
            //移动棋子
            log.info(socketMessage.getContent());
            sessionMap.get(isPlayingMap.get(session)).sendMessage(socketMessage);
        } else if (StringUtils.equals(socketMessage.getType(), ChessStatus.CHAT.getType())) {
            sessionMap.get(isPlayingMap.get(session)).sendMessage(socketMessage);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Session other = isPlayingMap.get(session);
        //如果此人正在游戏中   那么通知另外一个人 游戏赢了
        if (other != null) {
            sessionMap.get(other).sendMessage(SocketMessage.builder().type(ChessStatus.LEAVE.getType()).build());
            isPlayingMap.remove(session);
            isPlayingMap.remove(other);
        }
        sessionMap.remove(session);
        this.session = null;
        log.info("拜拜,当前在线人数:{}人", sessionMap.size());
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public void sendMessage(SocketMessage msg) {
        try {
            this.session.getBasicRemote().sendText(JSONObject.toJSONString(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
