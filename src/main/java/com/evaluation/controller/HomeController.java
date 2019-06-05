package com.evaluation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/**")
public class HomeController {


    @GetMapping("/login")
    public void adminLogin() {
    }

    @GetMapping("/accessDenied")
    public void adminAccessDenied() {

    }

    @GetMapping("/logout")
    public void adminLogout() {

    }
}