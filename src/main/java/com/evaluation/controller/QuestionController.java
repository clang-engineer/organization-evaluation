package com.evaluation.controller;

import java.util.Optional;

import com.evaluation.domain.Question;
import com.evaluation.service.DistinctInfoService;
import com.evaluation.service.QuestionService;
import com.evaluation.service.StaffService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/question/*")
@Slf4j
public class QuestionController {

    @Setter(onMethod_ = { @Autowired })
    QuestionService questionService;

    @Setter(onMethod_ = { @Autowired })
    StaffService staffService;

    @Setter(onMethod_ = { @Autowired })
    DistinctInfoService distinctInfoservice;

    @GetMapping("/register")
    public void register(long tno, PageVO vo, Model model) {
        log.info("register get by " + tno + vo);

        model.addAttribute("tno", tno);
        model.addAttribute("distinctInfo", distinctInfoservice.getDistinctQuestionInfo(tno));
    }

    @PostMapping("/register")
    public String register(Question question, long tno, RedirectAttributes rttr) {
        log.info("register post by " + question);

        question.setTno(tno);
        questionService.register(question);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);
        return "redirect:/question/list";
    }

    @GetMapping("/view")
    public void read(long qno, long tno, PageVO vo, Model model) {
        log.info("view by " + tno + vo);

        Optional<Question> question = questionService.read(qno);

        model.addAttribute("tno", tno);
        model.addAttribute("distinctInfo", distinctInfoservice.getDistinctQuestionInfo(tno));

        Question result = question.get();
        model.addAttribute("question", result);
    }

    @GetMapping("/modify")
    public void modify(long qno, long tno, PageVO vo, Model model) {
        log.info("modify by " + tno + vo);

        Optional<Question> question = questionService.read(qno);

        model.addAttribute("tno", tno);
        model.addAttribute("distinctInfo", distinctInfoservice.getDistinctQuestionInfo(tno));

        Question result = question.get();
        model.addAttribute("question", result);
    }

    @PostMapping("/modify")
    public String modify(Question question, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("modify" + question);

        questionService.modify(question);
        rttr.addAttribute("tno", tno);
        rttr.addFlashAttribute("msg", "modify");
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/question/list";
    }

    @PostMapping("/remove")
    public String remove(long qno, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + qno);

        questionService.remove(qno);

        rttr.addAttribute("tno", tno);
        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/question/list";
    }

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("question list by " + tno + vo);

        model.addAttribute("tno", tno);

        Page<Question> result = questionService.getList(tno, vo);
        log.info("====================" + result.get());
        model.addAttribute("result", new PageMaker<>(result));
    }

}
