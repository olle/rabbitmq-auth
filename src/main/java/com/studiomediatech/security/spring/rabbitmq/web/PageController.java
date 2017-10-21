package com.studiomediatech.security.spring.rabbitmq.web;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    @RequestMapping({ "/", "/index" })
    public String index() {

        return "index";
    }


    @GetMapping("/user")
    public String user() {

        return "user";
    }


    @GetMapping("/admin")
    public String admin() {

        return "admin";
    }
}
