package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
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
@RequestMapping("/relation360/*")
@Slf4j
public class Relation360Controller {

    @Setter(onMethod_ = { @Autowired })
    Relation360Service relation360Service;

    @Setter(onMethod_ = { @Autowired })
    StaffService staffService;

    @Setter(onMethod_ = { @Autowired })
    TurnService turnService;

    @PostMapping("/register")
    public String register(Relation360 relation360, PageVO vo, RedirectAttributes rttr) {
        log.info("register " + relation360.getEvaluated().getSno());

        relation360Service.register(relation360);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", relation360.getTno());
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relation360/list";
    }

    @PostMapping("/remove")
    public String remove(long rno, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + tno + rno);

        relation360Service.remove(rno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relation360/list";
    }

    @GetMapping("/list")
    public void getList(long tno, PageVO vo, Model model) {
        log.info("getList by " + tno);

        model.addAttribute("tno", tno);

        List<Relation360> relationTable = relation360Service.getAllList(tno);
        model.addAttribute("relationTable", relationTable);

        Page<Staff> result = relation360Service.getDistinctEvaluatedList(tno, vo);
        model.addAttribute("result", new PageMaker<>(result));
    }

    @PostMapping("/removeRow")
    public String deleteEvaluatedInfo(long tno, long sno, PageVO vo, RedirectAttributes rttr) {
        log.info("deleteEvaluatedInfo by " + tno);

        relation360Service.deleteEvaluatedInfo(tno, sno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relation360/list";
    }

}