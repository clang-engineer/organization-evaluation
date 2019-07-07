package com.evaluation.controller;

import com.evaluation.service.TurnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/comment/*")
@Slf4j
public class CommentController {

    @Autowired
    TurnService turnService;

    @PostMapping("/register")
    public String register(long tno, String comment, RedirectAttributes rttr) {
        log.info("add " + comment);

        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().add(comment);
            turnService.commentRegister(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/comment/list";
    }

    @PostMapping("/modify")
    public String modify(long tno, int idx, String comment, RedirectAttributes rttr) {
        log.info("Modify " + comment);

        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().set(idx, comment);
            turnService.commentRegister(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/comment/list";
    }

    @PostMapping("/remove")
    public String remove(long tno, int idx, RedirectAttributes rttr) {
        log.info("remove " + idx);
        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().remove(idx);
            turnService.commentRegister(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/comment/list";
    }

    @GetMapping("/list")
    public void readList(long tno, Model model) {
        log.info("question list by " + tno);

        turnService.read(tno).ifPresent(turn -> {
            log.info("" + turn.getComments());
            model.addAttribute("commentList", turn.getComments());
        });
        model.addAttribute("tno", tno);

    }
}