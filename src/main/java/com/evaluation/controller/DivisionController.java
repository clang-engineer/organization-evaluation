package com.evaluation.controller;

import com.evaluation.domain.Division;
import com.evaluation.service.DivisionService;
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

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/division/*")
@Slf4j
public class DivisionController {

    @Autowired
    DivisionService divisionService;

    @Autowired
    TurnService turnService;

    @PostMapping("/register")
    public String register(long tno, Division division, RedirectAttributes rttr) {
        log.info("division register by " + tno + division);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCompany().getCno();
        division.setCno(cno);

        divisionService.register(division);

        return "redirect:/division/list";
    }

    @PostMapping("/modify")
    public String modify(Division division, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("modify " + division);

        divisionService.modify(division);

        rttr.addFlashAttribute("msg", "modify");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/division/list";
    }

    @PostMapping("/remove")
    public String remove(long dno, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + dno);

        divisionService.remove(dno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/division/list";
    }

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("division list by " + tno);

        model.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCompany().getCno();
        Page<Division> result = divisionService.getListWithPaging(cno, vo);
        model.addAttribute("result", new PageMaker<>(result));

    }

}
