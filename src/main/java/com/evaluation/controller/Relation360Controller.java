package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.service.Relation360Service;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/relation360/*")
@Slf4j
public class Relation360Controller {

    @Setter(onMethod_ = { @Autowired })
    Relation360Service relation360Service;

    @RequestMapping("/list")
    public void getList(long tno, PageVO vo, Model model) {
        log.info("getList by " + tno);

        model.addAttribute("tno", tno);

        List<Relation360> relationTable = relation360Service.getAllList(tno);
        model.addAttribute("relationTable", relationTable);

        Page<Staff> result = relation360Service.getDistinctEvaluatedList(tno, vo);
        log.info("===>" + result.getContent());
        model.addAttribute("result", new PageMaker<>(result));
    }

}