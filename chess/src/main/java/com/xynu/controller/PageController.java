package com.xynu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author xiaosuda
 * @date 2018/3/26
 */
@RequestMapping("/index/")
@Controller
public class PageController {


    @RequestMapping("{page}")
    public ModelAndView toPage(@PathVariable("page") String page) {
        return new ModelAndView(page);
    }
}
