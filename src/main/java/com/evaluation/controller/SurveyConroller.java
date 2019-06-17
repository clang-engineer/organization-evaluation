package com.evaluation.controller;

import java.util.HashMap;
import java.util.HashSet;
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

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class SurveyConroller {

    CompanyService companyService;

    TurnService turnService;

    Relation360Service relation360Service;

    QuestionService questionService;

    BookService bookService;

    @GetMapping("/")
    public String survey(String company, Model model) {
        log.info("====>survey by company" + company);

        // 회사에 관한 정보 찾고
        companyService.readByCompanyId(company).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("company", origin);
            // 회사 cno로 turn정보를 찾는다.
            turnService.getListInSurvey(cno).ifPresent(turn -> {
                model.addAttribute("turns", turn);
            });
        });

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

        // Staff evaluator = relation360Service.findInEvaluator(tno, staff.getEmail());
        relation360Service.findInEvaluator(tno, staff.getEmail()).ifPresent(evaluator -> {
            if (evaluator.getPassword().equals(staff.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("evaluator", evaluator);
            }
        });

        rttr.addAttribute("tno", tno);

        // 로그인 실패하면 메인으로 가도 세션없어서 로그인 폼으로 감! 패스워드 일치여부에 관계없이 메인으로 리다이렉트.
        return "redirect:/survey/main";
    }

    @PostMapping("/logout")
    public String userLogOut(String company, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("log out!");

        HttpSession session = request.getSession();
        session.invalidate();

        rttr.addAttribute("company", company);

        return "redirect:/survey/";
    }

    @GetMapping("/contact")
    public void contact(String company, Long tno, Model model) {

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        companyService.readByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

    }

    @GetMapping("/main")
    public String main(String company, Long tno, Model model, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("====>turn main by company" + company);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);

        companyService.readByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        questionService.DistinctDivisionCountByTno(tno).ifPresent(origin -> {
            model.addAttribute("question", origin);
        });

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

        companyService.readByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        model.addAttribute("tno", tno);
        model.addAttribute("company", company);

        relation360Service.findByEvaluator(evaluator.getSno(), tno).ifPresent(relation -> {
            Set<String> relationList = new HashSet<>();
            relation.forEach(origin -> {
                relationList.add(origin.getRelation());
            });
            model.addAttribute("relationList", relationList);
            model.addAttribute("evaluatedList", relation);
        });

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

        companyService.readByCompanyId(company).ifPresent(origin -> {
            rttr.addAttribute("companyInfo", origin);
        });

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

        companyService.readByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        // 회차에 속하는 comment list를 추가하기 위한.
        turnService.get(tno).ifPresent(turn -> {
            model.addAttribute("commentList", turn.getComments());
            // 회답지 추가
            bookService.read(turn.getInfo360().getReplyCode()).ifPresent(book -> {
                model.addAttribute("book", book.getContents());
            });
        });

        // 관계 정보가 존재하는 경우에 작동
        relation360Service.read(rno).ifPresent(relation -> {
            // 관계에 대한 정보 추가
            model.addAttribute("relation", relation);

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
    public String submit(long rno, String finish, @RequestParam Map<String, String> answer, RedirectAttributes rttr) {
        log.info("" + answer + rno + finish);

        // 전체 맵에서 객관식과 주관식 나누기
        Set<Map.Entry<String, String>> entries = answer.entrySet();
        Map<String, Double> tmpAnswers = new HashMap<String, Double>();
        Map<String, String> tmpComments = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().substring(0, 1).equals("q")) {
                tmpAnswers.put(entry.getKey(), Double.parseDouble(entry.getValue()));
            } else if (entry.getKey().substring(0, 1).equals("c")) {
                tmpComments.put(entry.getKey(), entry.getValue());
            }
        }

        relation360Service.read(rno).ifPresent(origin -> {
            // relation객체를 만든 후 수정 함수로 보내기
            origin.setAnswers(tmpAnswers);
            origin.setComments(tmpComments);

            // 저장 상태 고르기
            origin.setFinish(finish);

            relation360Service.modify(origin);

            // redirect 속성 만들기
            long tno = origin.getTno();
            turnService.get(tno).ifPresent(turn -> {
                long cno = turn.getCno();
                companyService.get(cno).ifPresent(company -> {
                    rttr.addAttribute("company", company.getId());
                    rttr.addFlashAttribute("companyInfo", company);
                    rttr.addAttribute("tno", tno);
                });
            });
        });

        return "redirect:/survey/list";
    }
}