package com.evaluation.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.evaluation.domain.Turn;
import com.evaluation.service.TurnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * <code>CommentController</code>객체는 주관식 문항 정보를 관리한다.
 */
@Controller
@RequestMapping("/turns/{tno}/types")
@Slf4j
public class AppellationController {

    @Autowired
    TurnService turnService;

    /**
     * 호칭 정보를 등록한다.
     * 
     * @param tno         회차 id
     * @param appellation 호칭 정보
     * @param request     요청 정보
     * @return 상태 메시지
     */
    @PostMapping(value = { "/survey/appellations", "/mbo/appellations" })
    public ResponseEntity<HttpStatus> register(@PathVariable("tno") long tno, @RequestBody String appellation,
            HttpServletRequest request) {
        log.info("register " + appellation);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[4];

        turnService.read(tno).ifPresent(turn -> {
            if ("survey".equals(pathInfo)) {
                turn.getSurveyAppellation().add(appellation);
            } else if ("mbo".equals(pathInfo)) {
                turn.getMboAppellation().add(appellation);
            }
            turnService.register(turn);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 호칭 정보를 등록한다.
     * 
     * @param tno         회차 id
     * @param appellation 호칭 정보
     * @param request     요청 정보
     * @return 호칭
     */
    @GetMapping(value = { "/survey/appellations/{idx}", "/mbo/appellations/{idx}" })
    @ResponseBody
    public ResponseEntity<String> read(@PathVariable("tno") long tno, @PathVariable("idx") int idx,
            HttpServletRequest request) {
        log.info("read " + tno + "/" + idx);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[4];

        List<String> appellation = new ArrayList<>();
        if ("survey".equals(pathInfo)) {
            appellation = turnService.read(tno).map(Turn::getSurveyAppellation).orElse(null);
        } else if ("mbo".equals(pathInfo)) {
            appellation = turnService.read(tno).map(Turn::getMboAppellation).orElse(null);
        }

        return new ResponseEntity<>(appellation.get(idx), HttpStatus.OK);
    }

    /**
     * 호칭 정보를 수정한다.
     *
     * @param tno         회차 id
     * @param idx         리스트 index
     * @param appellation 주관식 문항
     * @param request     요청 정보
     * @return 상태 메시지
     */
    @PutMapping(value = { "/survey/appellations/{idx}", "/mbo/appellations/{idx}" })
    public ResponseEntity<HttpStatus> modify(@PathVariable("tno") long tno, @PathVariable("idx") int idx,
            @RequestBody String appellation, HttpServletRequest request) {
        log.info("Modify " + appellation);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[4];

        turnService.read(tno).ifPresent(turn -> {
            if ("survey".equals(pathInfo)) {
                turn.getSurveyAppellation().set(idx, appellation);
            } else if ("mbo".equals(pathInfo)) {
                turn.getMboAppellation().set(idx, appellation);
            }
            turnService.register(turn);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 호칭 문항을 삭제한다.
     *
     * @param tno 회차 id
     * @param idx 리스트 index
     * @return 상태 메시지
     */
    @DeleteMapping(value = { "/survey/appellations/{idx}", "/mbo/appellations/{idx}" })
    public ResponseEntity<HttpStatus> remove(@PathVariable("tno") long tno, @PathVariable("idx") int idx,
            HttpServletRequest request) {
        log.info("remove " + idx);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[4];

        turnService.read(tno).ifPresent(turn -> {
            if ("survey".equals(pathInfo)) {
                turn.getSurveyAppellation().remove(idx);
            } else if ("mbo".equals(pathInfo)) {
                turn.getMboAppellation().remove(idx);
            }
            turnService.register(turn);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회차에 속하는 주관식 목록을 전달한다.
     * 
     * @param tno     회차 id
     * @param request 요청 정보
     * @param model   화면 전달 정보
     * @param return  호칭 리스트 화면
     */
    @GetMapping(value = { "/survey/appellations/list", "/mbo/appellations/list" })
    public String readList(@PathVariable("tno") long tno, HttpServletRequest request, Model model) {
        log.info("appellation list by " + tno);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[4];

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