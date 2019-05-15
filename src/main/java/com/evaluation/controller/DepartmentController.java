package com.evaluation.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/department/*")
@Slf4j
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TurnService turnService;

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("controller : department list by " + tno);

        model.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCompany().getCno();
        Page<Department> result = departmentService.getListWithPaging(cno, vo);
        model.addAttribute("result", new PageMaker<>(result));

    }

}
