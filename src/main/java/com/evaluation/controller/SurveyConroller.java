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
import com.evaluation.service.RelationSurveyService;
import com.evaluation.service.StaffService;
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

/**
 * <code>SurveyConroller</code>객체는 survey를 관리한다.
 */
@Controller
@RequestMapping("/survey")
@Slf4j
@AllArgsConstructor
@Transactional
public class SurveyConroller {

    CompanyService companyService;

    TurnService turnService;

    RelationSurveyService relationSurveyService;

    QuestionService questionService;

    BookService bookService;

    StaffService staffService;

    /**
     * 로그인 페이지에 회차 정보를 읽어온다.
     * 
     * @param company 회사 이름
     * @param model   화면 전달 정보
     * @return Survey 로그인 페이지
     */
    @GetMapping("")
    public String login(String company, Model model) {
        log.info("====>survey by company" + company);

        // 회사에 관한 정보 찾고
        companyService.findByCompanyId(company).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("company", origin);
            // 회사 cno로 turn정보를 찾는다.
            turnService.getTurnsInSurvey(cno).ifPresent(turn -> {
                model.addAttribute("turns", turn);
            });
        });

        return "/survey/index";
    }

    /**
     * 로그인 처리를 한다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param staff   직원 정보
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @return Survey 메인 페이지
     */
    @PostMapping("/login")
    public String login(String company, long tno, Staff staff, RedirectAttributes rttr, HttpServletRequest request) {
        log.info("user login" + tno + staff);

        rttr.addAttribute("company", company);

        if (tno == 0) {
            rttr.addFlashAttribute("error", "tno");
            return "redirect:/survey/";
        }

        if (!relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).isPresent()) {
            rttr.addFlashAttribute("error", "email");
            return "redirect:/survey/";
        } else if (!relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).get().getPassword()
                .equals(staff.getPassword())) {
            rttr.addFlashAttribute("error", "password");
            return "redirect:/survey/";
        }

        relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).ifPresent(evaluator -> {
            if (evaluator.getPassword().equals(staff.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("evaluator", evaluator);
            }
        });

        rttr.addAttribute("tno", tno);

        // 로그인 실패하면 메인으로 가도 세션없어서 로그인 폼으로 감! 패스워드 일치여부에 관계없이 메인으로 리다이렉트.
        return "redirect:/survey/main";
    }

    /**
     * 로그아웃 처리를 한다.
     * 
     * @param company 회자 이름
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @return 로그인 페이지
     */
    @PostMapping("/logout")
    public String userLogOut(String company, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("log out!");

        HttpSession session = request.getSession();
        session.invalidate();

        rttr.addAttribute("company", company);

        return "redirect:/survey/";
    }

    /**
     * 문의 사항 페이지
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param model   화면 전달 정보
     */
    @GetMapping("/contact")
    public void contact(String company, Long tno, Model model) {

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });
    }

    /**
     * 메인 페이지를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @param model   화면 전달 정보
     * @return Survey 메인 페이자
     */
    @GetMapping("/main")
    public String main(String company, Long tno, HttpServletRequest request, RedirectAttributes rttr, Model model) {
        log.info("====>turn main by company" + company);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);

        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        questionService.getDistinctDivisionCountByTno(tno).ifPresent(origin -> {
            model.addAttribute("question", origin);
        });

        turnService.read(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
        });

        return "/survey/main";
    }

    /**
     * Survey목록 페이지를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param request 요청 객체 정보
     * @param model   화면 전달 정보
     * @param rttr    재전송 정보
     * @return Survey 대상자 목록 페이지
     */
    @GetMapping("/list")
    public String list(String company, long tno, HttpServletRequest request, Model model, RedirectAttributes rttr) {
        log.info("====>turn list by company" + company);
        HttpSession session = request.getSession();
        Staff evaluator = (Staff) session.getAttribute("evaluator");

        if (evaluator == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        model.addAttribute("tno", tno);
        model.addAttribute("company", company);

        relationSurveyService.findByEvaluator(tno, evaluator.getSno()).ifPresent(relation -> {
            Set<String> relationList = new HashSet<>();
            relation.forEach(origin -> {
                relationList.add(origin.getRelation());
            });
            model.addAttribute("relationList", relationList);
            model.addAttribute("evaluatedList", relation);
        });

        return "/survey/list";
    }

    /**
     * Survey 페이지에서 새로 고침시 목록 페이지로 재전송한다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param request 요청 객체 정보
     * @param rttr    재전송 정보
     * @return Survey 대상자 목록 페이지
     */
    @GetMapping("/evaluate")
    public String evaluate(String company, long tno, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("" + tno);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey/";
        }

        companyService.findByCompanyId(company).ifPresent(origin -> {
            rttr.addAttribute("companyInfo", origin);
        });

        rttr.addAttribute("company", company);
        rttr.addAttribute("tno", tno);
        return "redirect:/survey/list";
    }

    /**
     * 피평가자에 대한 서베이를 실시한다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param rno     관계 id
     * @param model   화면 전달 정보
     */
    // @GetMapping("/evaluate")
    @PostMapping("/evaluate")
    public void evaluate(String company, long tno, Long rno, Model model) {
        log.info("" + rno);

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);

        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        // 회차에 속하는 comment list를 추가하기 위한.
        turnService.read(tno).ifPresent(turn -> {
            model.addAttribute("commentList", turn.getComments());
            // 회답지 추가
            Integer replyCode = turn.getInfoSurvey().getReplyCode();
            if (replyCode != null) {
                bookService.read(replyCode).ifPresent(book -> {
                    model.addAttribute("book", book.getContents());
                });
            }
        });

        // 관계 정보가 존재하는 경우에 작동
        relationSurveyService.read(rno).ifPresent(relation -> {
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
                            category.add(q.getCategory());
                        });
                        model.addAttribute("category", category);
                        model.addAttribute("questionList", question);
                    });

        });
    }

    /**
     * 평가를 제출한다.
     * 
     * @param rno    관계 id
     * @param finish 완료 여부
     * @param answer 회답 정보
     * @param rttr   재전송 정보
     * @return 목록 페이지
     */
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

        relationSurveyService.read(rno).ifPresent(origin -> {
            // relation객체를 만든 후 수정 함수로 보내기
            origin.setAnswers(tmpAnswers);
            origin.setComments(tmpComments);

            // 저장 상태 고르기
            origin.setFinish(finish);

            relationSurveyService.modify(origin);

            // redirect 속성 만들기
            long tno = origin.getTno();
            turnService.read(tno).ifPresent(turn -> {
                long cno = turn.getCno();
                companyService.read(cno).ifPresent(company -> {
                    rttr.addAttribute("company", company.getId());
                    rttr.addFlashAttribute("companyInfo", company);
                    rttr.addAttribute("tno", tno);
                });
            });
        });

        return "redirect:/survey/list";
    }

    /**
     * 사용자 정보를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param model   화면 전달 정보
     */
    @GetMapping("/profile")
    public void profile(String company, long tno, Model model) {

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

    }

    /**
     * 사용자 정보 수정 페이지를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param model   화면 전달 정보
     */
    @GetMapping("/modify")
    public void modify(String company, long tno, Model model) {

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

    }

    /**
     * 사용자 정보를 수정한다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param staff   직원 정보
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @return 사용자 프로필 페이지
     */
    @PostMapping("/modify")
    public String modify(Staff staff, String company, long tno, RedirectAttributes rttr, HttpServletRequest request) {
        staffService.findByEmail(staff.getEmail()).ifPresent(origin -> {
            long sno = origin.getSno();
            log.info("===>" + sno);
            staff.setSno(sno);
            staffService.modify(staff);

            HttpSession session = request.getSession();
            session.setAttribute("evaluator", staff);
        });

        rttr.addAttribute("company", company);
        rttr.addAttribute("tno", tno);
        companyService.findByCompanyId(company).ifPresent(origin -> {
            rttr.addFlashAttribute("companyInfo", origin);
        });

        return "redirect:/survey/profile";
    }
}