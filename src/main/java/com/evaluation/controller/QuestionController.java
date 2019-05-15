package com.evaluation.controller;

import java.util.Optional;

import com.evaluation.domain.Question;
import com.evaluation.domain.Turn;
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

    @GetMapping("/register")
    public void register(long tno, PageVO vo, Model model) {
        log.info("Question register get by " + tno + vo);

        model.addAttribute("tno", tno);
    }

    @PostMapping("/register")
    public String register(Question question, long tno, RedirectAttributes rttr) {
        log.info("Question register post by " + question);

        Turn turn = new Turn();
        turn.setTno(tno);
        question.setTurn(turn);
        questionService.register(question);

        rttr.addFlashAttribute("msg", "success");
        rttr.addAttribute("tno", tno);
        return "redirect:/question/list";
    }

    @GetMapping("/view")
    public void read(long qno, long tno, PageVO vo, Model model) {
        log.info("controller : staff read by " + tno + vo);

        Optional<Question> question = questionService.read(qno);

        model.addAttribute("tno", tno);

        Question result = question.get();
        model.addAttribute("staff", result);
    }

    // @GetMapping("/modify")
    // public void modify(long sno, long tno, PageVO vo, Model model) {
    // log.info("controller : staff modify by " + tno + vo);

    // Optional<Staff> staff = staffService.read(sno);

    // model.addAttribute("tno", tno);
    // Staff result = staff.get();
    // model.addAttribute("staff", result);
    // }

    // @PostMapping("/modify")
    // public String modify(Staff staff, long tno, PageVO vo, RedirectAttributes
    // rttr) {
    // log.info("controller : staff modify post by " + staff.getName());

    // staffService.read(staff.getSno()).ifPresent(origin -> {
    // origin.setEmail(staff.getEmail());
    // origin.setName(staff.getName());
    // origin.setId(staff.getId());
    // origin.setPassword(staff.getPassword());
    // origin.setDepartment1(staff.getDepartment1());
    // origin.setDepartment2(staff.getDepartment2());
    // origin.setLevel(staff.getLevel());
    // origin.setDivision1(staff.getDivision1());
    // origin.setDivision2(staff.getDivision2());
    // staffService.modify(origin);
    // rttr.addFlashAttribute("msg", "success");
    // });

    // rttr.addAttribute("tno", tno);
    // rttr.addAttribute("page", vo.getPage());
    // rttr.addAttribute("size", vo.getSize());
    // rttr.addAttribute("type", vo.getType());
    // rttr.addAttribute("keyword", vo.getKeyword());
    // return "redirect:/staff/list";
    // }

    // @PostMapping("/remove")
    // public String remove(long sno, long tno, PageVO vo, RedirectAttributes rttr)
    // {
    // log.info("controller : staff delete by " + sno);

    // staffService.remove(sno);

    // rttr.addAttribute("tno", tno);
    // rttr.addAttribute("page", vo.getPage());
    // rttr.addAttribute("size", vo.getSize());
    // rttr.addAttribute("type", vo.getType());
    // rttr.addAttribute("keyword", vo.getKeyword());
    // return "redirect:/staff/list";
    // }

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("question list by " + tno + vo);

        model.addAttribute("tno", tno);

        Page<Question> result = questionService.getList(tno, vo);
        log.info("====================" + result.get());
        model.addAttribute("result", new PageMaker<>(result));
    }

}
