package com.xynu.intercepter;

import com.xynu.bo.UnCheckLogin;
import com.xynu.util.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 *
 * @author xiaosuda
 * @date 2018/1/9
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURL().toString();

        if (StringUtils.endsWith(url, "/login")) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;


        UnCheckLogin methodAnnotation = handlerMethod.getMethodAnnotation(UnCheckLogin.class);
        UnCheckLogin declaredAnnotation = handlerMethod.getBeanType().getDeclaredAnnotation(UnCheckLogin.class);
        //如果没有UnCheckLogin的注释  那么就需要进行验证
        if (methodAnnotation == null && declaredAnnotation == null) {
            String username = AppUtils.getValueFromCookie(request, "username");
            if (username == null) {
                AppUtils.removeCookieByValue(response, "username");
                response.sendRedirect("/index/login");
                return false;
            }
        }
        return true;
    }



}
