package com.evaluation.controller;

import com.evaluation.domain.Admin;
import com.evaluation.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * <code>AdminController</code>객체는 관리자 정보를 관리한다.
 */
@Controller
@RequestMapping("/admin/*")
@Slf4j
@Transactional
public class AdminController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AdminService adminService;

    /**
     * 관리자 등록 화면을 표시한다.
     */
    @GetMapping("/register")
    public void register() {
        log.info("register get");
    }

    /**
     * 관리자 정보를 등록한다.
     * 
     * @param admin 관리자 정보 객체
     * @param rttr  재전송 정보
     * @return 관리자 목록 페이지
     */
    @PostMapping("/register")
    public String register(Admin admin, RedirectAttributes rttr) {
        log.info("register post" + admin);

        String encryptPw = passwordEncoder.encode(admin.getUpw());
        admin.setUpw(encryptPw);

        adminService.register(admin);

        rttr.addFlashAttribute("msg", "register");
        return "redirect:/admin/list";
    }

    /**
     * 관리자 정보를 읽어온다.
     * 
     * @param uid   관리자 id
     * @param model 화면 전달 정보
     */
    @GetMapping("/read")
    public void read(String uid, Model model) {
        log.info("read by " + uid);

        adminService.read(uid).ifPresent(origin -> {
            model.addAttribute("result", origin);
        });
    }

    /**
     * 관리자 정보를 수정한다.
     * 
     * @param admin 관리자 정보 객체
     * @param rttr  재전송 정보
     * @return 관리자 목록 페이지
     */
    @PostMapping("/modify")
    public String modify(Admin admin, RedirectAttributes rttr) {
        log.info("modify " + admin);

        String encryptPw = passwordEncoder.encode(admin.getUpw());
        admin.setUpw(encryptPw);

        adminService.modify(admin);

        rttr.addFlashAttribute("msg", "modify");
        return "redirect:/admin/list";
    }

    /**
     * 관리자 정보를 삭제한다.
     * 
     * @param uid  관리자 id
     * @param rttr 재전송 정보
     * @return 관리자 목록 페이지
     */
    @PostMapping("/remove")
    public String remove(String uid, RedirectAttributes rttr) {
        log.info("delete " + uid);

        if (uid != null) {
            adminService.remove(uid);
        }
        rttr.addFlashAttribute("msg", "remove");
        return "redirect:/admin/list";

    }

    /**
     * 관리자 목록을 불러온다.
     * 
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void list(Model model) {
        adminService.list().ifPresent(admin -> {
            model.addAttribute("result", admin);
        });
    }

}