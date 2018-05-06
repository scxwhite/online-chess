package com.xynu.controller;

import com.xynu.model.JsonResponse;
import com.xynu.service.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaosuda
 * @date 2018/5/6
 */
@Controller
@RequestMapping("/score/")
public class ScoreController {

    @Autowired
    private ScoresService scoresService;

    /**
     * 更新分数状态
     *
     * @param id   用户id
     * @param res 结果  1为胜利 0为失败
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse<Boolean> updateScore(Integer id, Integer res) {
        boolean updateStatus = scoresService.updateStatus(id, res);
        if (updateStatus) {
            return new JsonResponse<>(true, "更新分数成功");
        }
        return new JsonResponse<>(false, "更新分数失败");
    }
}
