package com.xynu.controller;

import com.xynu.model.RoomPlayer;
import com.xynu.model.ScoresVO;
import com.xynu.service.ScoresService;
import com.xynu.socket.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaosuda
 * @date 2018/3/26
 */
@RequestMapping("/index/")
@Controller
public class PageController {

    @Autowired
    private ScoresService scoresService;

    @RequestMapping("{page}")
    public ModelAndView toPage(@PathVariable("page") String page) {
        return new ModelAndView(page);
    }

    @RequestMapping("/model/start")
    public ModelAndView toStartPage() {
        ModelAndView modelAndView = new ModelAndView("start");
        List<ScoresVO> topScores = scoresService.getTopScores();
        modelAndView.addObject("scores", topScores);
        return modelAndView;
    }

    @RequestMapping("/model/room")
    public ModelAndView toRoomPage() {
        ModelAndView modelAndView = new ModelAndView("room");
        Map<Integer, RoomPlayer> playerMap = MyWebSocket.roomPlayerMap;
        List<RoomPlayer> res = new ArrayList<>(playerMap.size());
        playerMap.forEach((x, room) -> {
            res.add(room);
        });
        modelAndView.addObject("roomSize", res.size());
        modelAndView.addObject("rooms", res);

        return modelAndView;
    }


}
