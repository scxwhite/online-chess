package com.xynu.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 *
 * @author xiaosuda
 * @date 2018/5/6
 */
public class AppUtils {

    public static String getValueFromCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null ) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), key)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void removeCookieByValue(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
