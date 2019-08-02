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

/**
 * <code>CommentController</code>객체는 주관식 문항 정보를 관리한다.
 */
@Controller
@RequestMapping("/comment/*")
@Slf4j
public class CommentController {

    @Autowired
    TurnService turnService;

    /**
     * 주관식 정보를 등록한다.
     * 
     * @param tno     회차 id
     * @param comment 주관식 문항
     * @param rttr    재전송 정보
     * @return 주관식 목록
     */
    @PostMapping("/register")
    public String register(long tno, String comment, RedirectAttributes rttr) {
        log.info("add " + comment);

        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().add(comment);
            turnService.register(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/comment/list";
    }

    /**
     * 주관식 정보를 수정한다.
     * 
     * @param tno     회차 id
     * @param idx     리스트 index
     * @param comment 주관식 문항
     * @param rttr    재전송 정보
     * @return 주관식 목록
     */
    @PostMapping("/modify")
    public String modify(long tno, int idx, String comment, RedirectAttributes rttr) {
        log.info("Modify " + comment);

        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().set(idx, comment);
            turnService.register(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/comment/list";
    }

    /**
     * 주관식 문항을 삭제한다.
     * 
     * @param tno  회차 id
     * @param idx  리스트 index
     * @param rttr 재전송 정보
     * @return 주관식 목록
     */
    @PostMapping("/remove")
    public String remove(long tno, int idx, RedirectAttributes rttr) {
        log.info("remove " + idx);
        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().remove(idx);
            turnService.register(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/comment/list";
    }

    /**
     * 회차에 속하는 주관식 목록을 전달한다.
     * 
     * @param tno   회차 id
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void readList(long tno, Model model) {
        log.info("question list by " + tno);

        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });
    }
}