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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
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
     *
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
            Cookie usernameCookie = new Cookie("username", String.valueOf(id));
            Cookie addressCookie = new Cookie("address", getHostIp());
            usernameCookie.setPath("/");
            addressCookie.setPath("/");
            usernameCookie.setMaxAge(3 * 60 * 60);
            addressCookie.setMaxAge(3 * 60 * 60);
            response.addCookie(usernameCookie);
            response.addCookie(addressCookie);
            return new JsonResponse(true, "登录成功");
        }
        return new JsonResponse(false, "用户名或密码错误");
    }

    private String getHostIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":") == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
