package com.evaluation.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.evaluation.domain.Staff;
import com.evaluation.service.CompanyService;
import com.evaluation.service.QuestionService;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.TurnService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/survey/*")
@Slf4j
@AllArgsConstructor
public class SurveyConroller {

    CompanyService companyService;

    TurnService turnService;

    Relation360Service relation360Service;

    QuestionService questionService;

    @GetMapping("/")
    public String survey(String company, Model model) {
        log.info("====>survey by company" + company);

        long cno = companyService.readByCompanyId(company).getCno();

        model.addAttribute("company", companyService.readByCompanyId(company));
        model.addAttribute("turns", turnService.getListInSurvey(cno));

        return "/survey/index";
    }

    @PostMapping("/login")
    public String userLogin(long tno, Staff staff, RedirectAttributes rttr, HttpServletRequest request) {
        log.info("user login" + tno + staff);

        String company = companyService.get(turnService.get(tno).get().getCno()).get().getId();
        Staff evaluator = relation360Service.findInEvaluator(tno, staff.getEmail());

        rttr.addAttribute("company", company);

        if (evaluator.getPassword().equals(staff.getPassword())) {
            rttr.addAttribute("tno", tno);
            HttpSession session = request.getSession();
            session.setAttribute("evaluator", evaluator);
            return "redirect:/survey/main";
        } else {
            return "redirect:/survey/";
        }
    }

    @GetMapping("/main")
    public void main(String company, Long tno, Model model) {
        log.info("====>turn main by company" + company);

        long cno = companyService.readByCompanyId(company).getCno();

        model.addAttribute("question", questionService.DistinctDivisionCountByTno(tno));
        model.addAttribute("company", companyService.readByCompanyId(company));
        turnService.get(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
        });
    }

    @GetMapping("/list")
    public void list(String company, long tno, HttpServletRequest request, Model model) {
        log.info("====>turn list by company" + company);
        HttpSession session = request.getSession();
        Staff evaluator = (Staff) session.getAttribute("evaluator");

        model.addAttribute("evaluatedList", relation360Service.findByEvaluator(evaluator.getSno(), tno));

    }
}