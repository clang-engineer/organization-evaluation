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
    public String register(Relation360 relation360, RedirectAttributes rttr) {
        log.info("register " + relation360.getEvaluated().getSno());

        log.info("" + relation360.getTno());
        relation360Service.register(relation360);
        rttr.addAttribute("tno",  relation360.getTno());
        return "redirect:/relation360/list";
    }

    @GetMapping("/list")
    public void getList(long tno, PageVO vo, Model model) {
        log.info("getList by " + tno);

        model.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCompany().getCno();
        log.info("cno---?" + cno);
        model.addAttribute("cno", cno);

        List<Relation360> relationTable = relation360Service.getAllList(tno);
        model.addAttribute("relationTable", relationTable);

        Page<Staff> result = relation360Service.getDistinctEvaluatedList(tno, vo);
        log.info("===>" + result.getContent());
        model.addAttribute("result", new PageMaker<>(result));
    }

}