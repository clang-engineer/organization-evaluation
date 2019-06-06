package com.evaluation.controller;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.evaluation.domain.Staff;
import com.evaluation.service.BookService;
import com.evaluation.service.CompanyService;
import com.evaluation.service.QuestionService;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.TurnService;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    BookService bookService;

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

        turnService.get(tno).ifPresent(turn -> {
            long cno = turn.getCno();
            companyService.get(cno).ifPresent(origin -> {
                String company = origin.getId();
                rttr.addAttribute("company", company);
            });
        });
        
        Staff evaluator = relation360Service.findInEvaluator(tno, staff.getEmail());

        if (evaluator.getPassword().equals(staff.getPassword())) {
            rttr.addAttribute("tno", tno);
            HttpSession session = request.getSession();
            session.setAttribute("evaluator", evaluator);
            return "redirect:/survey/main";
        } else {
            return "redirect:/survey/";
        }
    }

    @PostMapping("/logout")
    public String userLogOut(String company, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("log out!");

        HttpSession session = request.getSession();
        session.invalidate();

        rttr.addAttribute("company", company);

        return "redirect:/survey/";
    }

    @GetMapping("/main")
    public String main(String company, Long tno, Model model, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("====>turn main by company" + company);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        long cno = companyService.readByCompanyId(company).getCno();

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        model.addAttribute("question", questionService.DistinctDivisionCountByTno(tno));
        model.addAttribute("companyInfo", companyService.readByCompanyId(company));
        turnService.get(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
        });
        return "/survey/main";
    }

    @GetMapping("/list")
    public String list(String company, long tno, HttpServletRequest request, Model model, RedirectAttributes rttr) {
        log.info("====>turn list by company" + company);
        HttpSession session = request.getSession();
        Staff evaluator = (Staff) session.getAttribute("evaluator");

        if (evaluator == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        model.addAttribute("tno", tno);
        model.addAttribute("company", company);
        model.addAttribute("evaluatedList", relation360Service.findByEvaluator(evaluator.getSno(), tno));

        return "/survey/list";
    }

    // 새로 고침시 리스트로 복귀하기 위한 매핑
    @GetMapping("/evaluate")
    public String evaluate(String company, long tno, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("" + tno);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        rttr.addAttribute("company", company);
        rttr.addAttribute("tno", tno);
        return "redirect:/survey/list";
    }

    // @GetMapping("/evaluate")
    @PostMapping("/evaluate")
    public void evaluate(Long rno, long tno, String company, Model model) {
        log.info("" + rno);

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);

        // 관계 정보가 존재하는 경우에 작동
        relation360Service.read(rno).ifPresent(relation -> {
            model.addAttribute("rno", rno);
            model.addAttribute("evaluated", relation.getEvaluated().getName());

            // 회차에 속하는 comment list를 추가하기 위한.
            turnService.get(relation.getTno()).ifPresent(turn -> {
                model.addAttribute("commentList", turn.getComments());
                bookService.read(turn.getInfo360().getReplyCode()).ifPresent(book -> {
                    model.addAttribute("book", book.getContents());
                });
            });

            // 개인의 division 정보와 일치하는 객관식 list를 추가하기 위한
            Staff evaluated = new Staff();
            evaluated = relation.getEvaluated();
            questionService.getListByDivision(relation.getTno(), evaluated.getDivision1(), evaluated.getDivision2())
                    .ifPresent(question -> {
                        // 중복 제거한 카테고리를 위해
                        Set<String> category = new LinkedHashSet<String>();
                        question.forEach(q -> {
                            category.add(q.get(1));
                        });
                        model.addAttribute("category", category);
                        model.addAttribute("questionList", question);
                    });

        });
    }

    @PostMapping("/submit")
    public void submit(@RequestParam Map<String, String> answer) {
        log.info("" + answer);
    }
}