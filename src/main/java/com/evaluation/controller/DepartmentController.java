package com.evaluation.controller;

import com.evaluation.domain.Department;
import com.evaluation.service.DepartmentService;
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
@RequestMapping("/department/*")
@Slf4j
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TurnService turnService;

    @PostMapping("/register")
    public String register(long tno, Department department, RedirectAttributes rttr) {
        log.info("department register by " + tno + department);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCno();
        department.setCno(cno);

        departmentService.register(department);

        return "redirect:/department/list";
    }

    @PostMapping("/modify")
    public String modify(Department department, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("modify " + department);


        departmentService.modify(department);
        rttr.addFlashAttribute("msg", "modify");

        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/department/list";
    }

    @PostMapping("/remove")
    public String remove(long dno, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + dno);

        departmentService.remove(dno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/department/list";
    }

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("department list by " + tno);

        model.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCno();
        Page<Department> result = departmentService.getListWithPaging(cno, vo);
        model.addAttribute("result", new PageMaker<>(result));

    }

}
