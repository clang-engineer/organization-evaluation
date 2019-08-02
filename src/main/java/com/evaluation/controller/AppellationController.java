package com.evaluation.controller;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/appellation/*")
@Slf4j
public class AppellationController {

    @Autowired
    TurnService turnService;

    /**
     * 호칭 정보를 등록한다.
     * 
     * @param tno         회차 id
     * @param appellation 호칭 정보
     * @param rttr        재전송 정보
     * @param request     요청 정보
     * @return 주관식 목록
     */
    @PostMapping(value = { "/survey/register", "/mbo/register" })
    public String register(long tno, String appellation, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("register " + appellation);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[2];

        turnService.read(tno).ifPresent(turn -> {
            if ("survey".equals(pathInfo)) {
                turn.getSurveyAppellation().add(appellation);
            } else if ("mbo".equals(pathInfo)) {
                turn.getMboAppellation().add(appellation);
            }
            turnService.register(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/appellation/" + pathInfo + "/list";
    }

    /**
     * 호칭 정보를 수정한다.
     *
     * @param tno         회차 id
     * @param idx         리스트 index
     * @param appellation 주관식 문항
     * @param request     요청 정보
     * @param rttr        재전송 정보
     * @return 주관식 목록
     */
    @PostMapping(value = { "/survey/modify", "/mbo/modify" })
    public String modify(long tno, int idx, String appellation, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("Modify " + appellation);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[2];

        turnService.read(tno).ifPresent(turn -> {
            if ("survey".equals(pathInfo)) {
                turn.getSurveyAppellation().set(idx, appellation);
            } else if ("mbo".equals(pathInfo)) {
                turn.getMboAppellation().set(idx, appellation);
            }
            turnService.register(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/appellation/" + pathInfo + "/list";
    }

    /**
     * 호칭 문항을 삭제한다.
     *
     * @param tno  회차 id
     * @param idx  리스트 index
     * @param rttr 재전송 정보
     * @return 주관식 목록
     */
    @PostMapping(value = { "/survey/remove", "/mbo/remove" })
    public String remove(long tno, int idx, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("remove " + idx);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[2];

        turnService.read(tno).ifPresent(turn -> {
            if ("survey".equals(pathInfo)) {
                turn.getSurveyAppellation().remove(idx);
            } else if ("mbo".equals(pathInfo)) {
                turn.getMboAppellation().remove(idx);
            }
            turnService.register(turn);
        });

        rttr.addAttribute("tno", tno);
        return "redirect:/appellation/" + pathInfo + "/list";
    }

    /**
     * 회차에 속하는 주관식 목록을 전달한다.
     * 
     * @param tno   회차 id
     * @param model 화면 전달 정보
     */
    @GetMapping(value = { "/survey/list", "/mbo/list" })
    public String readList(long tno, HttpServletRequest request, Model model) {
        log.info("appellation list by " + tno);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[2];

        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);

            if ("survey".equals(pathInfo)) {
                model.addAttribute("type", "survey");
            } else if ("mbo".equals(pathInfo)) {
                model.addAttribute("type", "mbo");
            }
        });

        return "appellation/list";
    }
}