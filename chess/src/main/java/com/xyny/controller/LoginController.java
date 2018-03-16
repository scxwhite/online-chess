package com.xyny.controller;

import com.xyny.entity.User;
import com.xyny.model.JsonResponse;
import com.xyny.service.UserService;
import com.xyny.util.ControllerUtil;
import com.xyny.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */
@Controller
@RequestMapping("/login/")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录校验
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public JsonResponse check(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            return new JsonResponse(false, "用户名或密码为空，请填写后再提交");
        }
        return ControllerUtil.getResponse(userService.loginCheck(User.builder().password(MD5Util.getMD5(password)).username(username).build()),
        "登录成功","用户名或密码错误");
    }
}
