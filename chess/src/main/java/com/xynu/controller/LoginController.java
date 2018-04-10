package com.xynu.controller;

import com.xynu.bo.UnCheckLogin;
import com.xynu.entity.User;
import com.xynu.model.JsonResponse;
import com.xynu.service.UserService;
import com.xynu.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @ResponseBody
    @UnCheckLogin
    public JsonResponse check(@RequestParam("username") String username,
                              @RequestParam(name = "password", required = false) String password,
                              HttpServletResponse response) throws IOException {
        Integer id;
        if (StringUtils.equals(username, "_visitor")) {
            id = -1;
        } else {
            id = userService.loginCheck(User.builder().username(username).password(MD5Util.getMD5(password)).build());
        }
        if (id != null) {
            Cookie cookie = new Cookie("username", String.valueOf(id));
            cookie.setPath("/");
            cookie.setMaxAge(3 * 60 * 60);
            response.addCookie(cookie);
            return new JsonResponse(true, "登录成功");
        }
        return new JsonResponse(false, "用户名或密码错误");
    }

}
