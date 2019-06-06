package com.evaluation.controller;

import com.evaluation.domain.Admin;
import com.evaluation.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/**")
@Slf4j
public class AdminController {

    @Setter(onMethod_ = { @Autowired })
    PasswordEncoder passwordEncoder;

    @Setter(onMethod_ = { @Autowired })
    AdminService adminService;

    @GetMapping("/register")
    public void register() {
        log.info("register get");
    }

    @PostMapping("/register")
    public String register(Admin admin, RedirectAttributes rttr) {
        log.info("register post" + admin);

        String encryptPw = passwordEncoder.encode(admin.getUpw());
        admin.setUpw(encryptPw);

        adminService.register(admin);

        rttr.addFlashAttribute("msg", "register");
        return "redirect:/admin/list";
    }

    @GetMapping("/read")
    public void read(String uid, Model model) {
        log.info("read by " + uid);

        model.addAttribute("result", adminService.read(uid).get());
    }

    @PostMapping("/modify")
    public String modify(Admin admin, RedirectAttributes rttr) {
        log.info("modify " + admin);

        String encryptPw = passwordEncoder.encode(admin.getUpw());
        admin.setUpw(encryptPw);

        adminService.modify(admin);

        rttr.addFlashAttribute("msg", "modify");
        return "redirect:/admin/list";
    }

    @PostMapping("/remove")
    public String remove(String uid, RedirectAttributes rttr) {
        log.info("delete " + uid);

        adminService.remove(uid);

        rttr.addFlashAttribute("msg", "remove");
        return "redirect:/admin/list";
    }

    @GetMapping("/list")
    public void list(Model model) {
        adminService.list().ifPresent(admin -> {
            model.addAttribute("result", admin);
        });
    }

}