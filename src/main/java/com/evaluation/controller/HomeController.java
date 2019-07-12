package com.evaluation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <code>HomeController</code>객체는 공통 화면 정보를 관리한다.
 */
@Controller
@RequestMapping("/*")
public class HomeController {

    /**
     * login 페이지를 읽어온다.
     */
    @GetMapping("/login")
    public void adminLogin() {
    }

    /**
     * login 실청했을 때의 페이지
     */
    @GetMapping("/accessDenied")
    public void adminAccessDenied() {
    }

    /**
     * 로그아웃 페이지
     */
    @GetMapping("/logout")
    public void adminLogout() {
    }
}