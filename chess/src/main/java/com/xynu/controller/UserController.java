package com.xynu.controller;

import com.xynu.bo.UnCheckLogin;
import com.xynu.entity.User;
import com.xynu.model.JsonResponse;
import com.xynu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author XIAOSUDA
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;
    private final String VISITOR_IMAGE = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2299480460,1050338730&fm=27&gp=0.jpg";

    @RequestMapping("headImage")
    @ResponseBody
    public JsonResponse<User> getUrl(Integer id) {
        //如果是游客
        if (id == -1) {
            User user = new User();
            user.setId(-1);
            user.setUsername("游客");
            user.setImageUrl(VISITOR_IMAGE);
            return new JsonResponse<>(true, "查询成功", user);
        }

        User user = userService.findById(id);
        if (user != null && user.getImageUrl() != null) {
            return new JsonResponse<>(true, "查询成功", user);
        }
        return new JsonResponse<>(false, "查询失败");
    }


    @RequestMapping("register")
    @ResponseBody
    @UnCheckLogin
    public JsonResponse<String> register(User user) {
       boolean res = userService.addUser(user);
       if (res) {
           return new JsonResponse<>(true, "注册成功，请登录");
       }
       return new JsonResponse<>(false, "注册失败，请联系管理员");
    }
}
