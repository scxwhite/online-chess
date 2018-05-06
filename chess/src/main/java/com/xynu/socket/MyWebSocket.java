package com.xynu.socket;

import com.alibaba.fastjson.JSONObject;
import com.xynu.model.ChessStatus;
import com.xynu.model.RoomPlayer;
import com.xynu.model.SocketMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;


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
    public  static Map<Integer, RoomPlayer>  roomPlayerMap = new ConcurrentHashMap<>();
    public  static Map<Session, Integer> sessionToRoomId = new ConcurrentHashMap<>();
    public  static AtomicInteger  roomIdGeneral = new AtomicInteger(0);
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
    public void  onMessage(String msg, Session session) {
        SocketMessage socketMessage;
        try {
            socketMessage = JSONObject.parseObject(msg, SocketMessage.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RoomPlayer player;
        switch (socketMessage.getType()) {
            case "prepare":
                Integer roomId = sessionToRoomId.get(session);
                //如果已经在房间准备
                if (roomId != null) {
                    player = roomPlayerMap.get(roomId);
                    if (player.getPeopleNum().get() < 2) {
                        sendMessage(new SocketMessage(ChessStatus.ERROE.getType(), "房间内必须两人才能进入准备状态"));
                        return ;
                    }
                    //如果两个人都在准备状态 开始游戏
                    if (player.getPrepareNum().addAndGet(1) >= 2) {
                        gameStart(player.getFirst(), player.getSecond());
                    } else {
                        sendMessage(new SocketMessage(ChessStatus.PREPARE.getType(), "已准备"));
                        if (Objects.equals(session, player.getFirst())) {
                            sessionMap.get(player.getSecond()).sendMessage(new SocketMessage(ChessStatus.PREPARE.getType(), "对方已准备"));
                        } else {
                            sessionMap.get(player.getFirst()).sendMessage(new SocketMessage(ChessStatus.PREPARE.getType(), "对方已准备"));
                        }
                    }
                } else {
                    //匹配模式  准备状态 想找对手
                    if (chessQueue.size() == 0) {
                        chessQueue.add(session);
                        log.info("进入队列等待对手~~");
                        sendMessage(new SocketMessage(ChessStatus.PREPARE.getType(), "正在匹配中...."));
                    } else if (chessQueue.contains(session)) {
                        log.info("已经在等待中。。。");
                    } else {
                        try {
                            Session otherSession = chessQueue.take();
                            gameStart(otherSession, session);
                        } catch (InterruptedException e) {
                            log.info("对手被抢走");
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "move" :
                //移动棋子
                Session otherSession = isPlayingMap.get(session);
                if (otherSession != null) {
                    sessionMap.get(otherSession).sendMessage(socketMessage);
                }
                break;
            case "chat" :
                otherSession = isPlayingMap.get(session);
                if (otherSession != null) {
                    sessionMap.get(otherSession).sendMessage(socketMessage);
                }
                break;
            case "createRoom" :
                //创建房间的请求
                player = new RoomPlayer();
                player.setPeopleNum(new AtomicInteger(1));
                player.setPrepareNum(new AtomicInteger(0));
                player.setFirst(session);
                player.setRoomName(socketMessage.getContent());
                player.setRoomId(roomIdGeneral.get());
                sessionToRoomId.put(session, roomIdGeneral.get());
                roomPlayerMap.put(roomIdGeneral.getAndAdd(1), player);
                sendMessage(new SocketMessage(ChessStatus.CREATE_ROOM.getType(), ""));
                break;
            case "enterRoom" :
                //进入房间
                player = roomPlayerMap.get(Integer.parseInt(socketMessage.getContent()));
                if (player == null) {
                    sendMessage(new SocketMessage(ChessStatus.ROOM_DESTROY.getType(), "该房间不存在"));
                } else {
                    if (player.getPeopleNum().compareAndSet(1, 2)) {
                        otherSession = null;
                        if (player.getFirst() == null) {
                            player.setFirst(session);
                            otherSession = player.getSecond();
                        } else if (player.getSecond() == null) {
                            player.setSecond(session);
                            otherSession = player.getFirst();
                        }
                        sessionToRoomId.put(session, player.getRoomId());
                        sendMessage(new SocketMessage(ChessStatus.ENTER_ROOM.getType(), sessionMap.get(otherSession).getUserId()));
                        sessionMap.get(otherSession).sendMessage(new SocketMessage(ChessStatus.ENTER_ROOM.getType(), userId));
                    } else {
                        SocketMessage response = new SocketMessage();
                        response.setType(ChessStatus.ERROE.getType());
                        response.setContent("该房间已满");
                        sendMessage(response);
                    }
                }
                break;
            case "roomStart" :
                //房间开始游戏
                player = roomPlayerMap.get(Integer.parseInt(socketMessage.getContent()));
                if (player != null) {
                    if (player.getFirst() != null && player.getSecond() != null) {
                        isPlayingMap.put(player.getFirst(), player.getSecond());
                        gameStart(player.getFirst(), player.getSecond());
                    }
                }
                break;
            default: break;
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Session other = isPlayingMap.get(session);
        //如果此人正在游戏中   那么通知另外一个人 游戏赢了
        if (other != null) {
            sessionMap.get(other).sendMessage(SocketMessage.builder().
                    type(ChessStatus.LEAVE.getType()).
                    content(userId).
                    build());
            isPlayingMap.remove(session);
            isPlayingMap.remove(other);
        }
        Integer roomId = sessionToRoomId.get(session);
        //如果此人在房间  则从房间删除
        if (roomId != null) {
            Session otherSession = null;
            RoomPlayer player = roomPlayerMap.get(roomId);
            if (player.getFirst() != null && Objects.equals(player.getFirst(), session)) {
                player.setFirst(null);
                int x = player.getPeopleNum().addAndGet(-1);
                if (x > 0) {
                    otherSession = player.getSecond();
                }
            }
            else if (player.getSecond() != null && Objects.equals(player.getSecond(), session)) {
                player.setSecond(null);
                int x = player.getPeopleNum().addAndGet(-1);
                if (x > 0) {
                    otherSession = player.getFirst();
                }
            }
            if (player.getPeopleNum().compareAndSet(0, 0)) {
                roomPlayerMap.remove(roomId);
                log.info("房间[{}]无人，房间销毁", roomId);
            } else if (otherSession != null){
                sessionMap.get(otherSession).sendMessage(SocketMessage.builder().
                        content("对方离开房间").
                        type(ChessStatus.LEAVE_ROOM.getType()).build());
                log.info("房间[{}]剩余一人，已通知", roomId);
            }
            sessionToRoomId.remove(session);
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

    private void gameStart(Session otherSession, Session session) {
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
    }
}
