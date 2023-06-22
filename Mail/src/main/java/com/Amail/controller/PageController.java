package com.Amail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 孔德昱
 * @date 2022/11/18 14:38 星期五
 */
@Controller
@RequestMapping("/page")
public class PageController {
    @RequestMapping("/all")
    public String DisplayAllPage(){
        return "main";
    }

    @RequestMapping("/top")
    public String DisplayTopPage(HttpServletRequest request){
        return "top";
    }

    @RequestMapping("/left")
    public String DisplayLeftPage(){
        return "sidebar";
    }

    @RequestMapping("/main")
    public String DisplayMainPage(){
        return "redirect:/getMails";
    }
}
