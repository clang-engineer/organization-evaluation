package com.evaluation.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.evaluation.domain.HelpDesk;
import com.evaluation.domain.Staff;
import com.evaluation.service.CompanyService;
import com.evaluation.service.HelpDeskService;
import com.evaluation.service.RelationMboService;
import com.evaluation.service.RelationSurveyService;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <code>HomeController</code>객체는 공통 화면 정보를 관리한다.
 */
@Controller
@RequestMapping("/*")
@Slf4j
@AllArgsConstructor
public class HomeController {

    HelpDeskService helpDeskService;

    TurnService turnService;

    CompanyService companyService;

    StaffService staffService;

    RelationMboService relationMboService;

    RelationSurveyService relationSurveyService;

    /**
     * 
     * @return 최초 화면
     */
    @GetMapping("")
    public String home() {
        return "redirect:/company/list";
    }

    /**
     * 관리자 login 페이지를 읽어온다.
     */
    @GetMapping("/login")
    public void adminLogin() {
    }

    /**
     * 관리자 login 실청했을 때의 페이지
     */
    @GetMapping("/accessDenied")
    public void adminAccessDenied() {
    }

    /**
     * 관리자 로그아웃 페이지
     */
    @GetMapping("/logout")
    public void adminLogout() {
    }

    /**
     * 사용자 로그인 페이지에 회차 정보를 읽어온다.
     * 
     * @param company 회사 이름
     * @param model   화면 전달 정보
     * @return Mbo로그인 페이지
     */
    @GetMapping(value = { "/survey", "/mbo" })
    public String userLogin(String company, HttpServletRequest request, Model model) {
        log.info("user login by " + company);

        String whatYouCall = request.getServletPath();

        // 회사에 관한 정보 찾고
        companyService.findByCompanyId(company).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("company", origin);
            // 회사 cno로 turn정보를 찾는다.
            if (whatYouCall.equals("/survey")) {
                turnService.getTurnsInSurvey(cno).ifPresent(turn -> {
                    model.addAttribute("turns", turn);
                });
            } else if (whatYouCall.equals("/mbo")) {
                turnService.getTurnsInMbo(cno).ifPresent(turn -> {
                    model.addAttribute("turns", turn);
                });
            }
        });

        return whatYouCall + "/index";
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
    @PostMapping("/survey/login")
    public String surveyLogin(String company, long tno, Staff staff, RedirectAttributes rttr,
            HttpServletRequest request) {
        log.info("user login by" + company + "/" + tno + "/" + staff);

        rttr.addAttribute("company", company);

        if (tno == 0) {
            rttr.addFlashAttribute("error", "tno");
            return "redirect:/survey";
        }

        if (!relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).isPresent()) {
            rttr.addFlashAttribute("error", "email");
            return "redirect:/survey";
        } else if (!relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).map(Staff::getPassword)
                .orElse(null).equals(staff.getPassword())) {
            rttr.addFlashAttribute("error", "password");
            return "redirect:/survey";
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
     * 로그인 처리를 한다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param staff   직원 정보
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @return mbo 메인 페이지
     */
    @PostMapping("/mbo/login")
    public String login(String company, long tno, Staff staff, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("user login by " + company + "/" + tno + "/" + staff);
        rttr.addAttribute("company", company);

        if (tno == 0) {
            rttr.addFlashAttribute("error", "tno");
            return "redirect:/mbo";
        }

        if (!relationMboService.findByEvaluatorEmail(tno, staff.getEmail()).isPresent()) {
            rttr.addFlashAttribute("error", "email");
            return "redirect:/mbo";
        } else if (!relationMboService.findByEvaluatorEmail(tno, staff.getEmail()).map(Staff::getPassword).orElse(null)
                .equals(staff.getPassword())) {
            rttr.addFlashAttribute("error", "password");
            return "redirect:/mbo";
        }

        relationMboService.findByEvaluatorEmail(tno, staff.getEmail()).ifPresent(evaluator -> {
            if (evaluator.getPassword().equals(staff.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("evaluator", evaluator);
            }
        });

        rttr.addAttribute("tno", tno);
        // 로그인 실패하면 메인으로 가도 세션없어서 로그인 폼으로 감! 패스워드 일치여부에 관계없이 메인으로 리다이렉트.
        return "redirect:/mbo/main";
    }

    /**
     * 로그아웃 처리를 한다.
     * 
     * @param company 회자 이름
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @return 로그인 페이지
     */
    @PostMapping(value = { "/survey/logout", "/mbo/logout" })
    public String userLogOut(String company, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("log out!");

        HttpSession session = request.getSession();
        session.invalidate();

        rttr.addAttribute("company", company);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        String redirectPath = "redirect:/" + pathInfo;
        return redirectPath;
    }

    /**
     * 사용자 정보를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param model   화면 전달 정보
     */
    @GetMapping(value = { "/survey/profile", "/mbo/profile" })
    public String userProfile(String company, long tno, HttpServletRequest request, Model model,
            RedirectAttributes rttr) {
        log.info("profile by " + company + "/" + tno);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey";
        }

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        // mbo turn에 따른 navbar 구분을 위해
        turnService.read(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
        });
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        String whatYouCall = request.getServletPath();

        return whatYouCall;
    }

    /**
     * 사용자 정보 수정 페이지를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param model   화면 전달 정보
     */
    @GetMapping(value = { "/survey/modify", "/mbo/modify" })
    public String userProfileModify(String company, long tno, HttpServletRequest request, Model model,
            RedirectAttributes rttr) {
        log.info("profile by " + company + "/" + tno);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey";
        }

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);

        // mbo turn에 따른 navbar 구분을 위해
        turnService.read(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
        });

        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        String whatYouCall = request.getServletPath();

        return whatYouCall;
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
    @PostMapping(value = { "/survey/modify", "/mbo/modify" })
    public String userProfileModify(String company, long tno, Staff staff, HttpServletRequest request,
            RedirectAttributes rttr) {
        staffService.findByEmail(staff.getEmail()).ifPresent(origin -> {
            long sno = origin.getSno();
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

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        String redirectPath = "redirect:/" + pathInfo + "/profile";
        return redirectPath;
    }

    /**
     * 문의 사항 페이지
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param model   화면 전달 정보
     */
    @GetMapping(value = { "/survey/contact", "/mbo/contact" })
    public String userContact(String company, Long tno, HttpServletRequest request, Model model,
            RedirectAttributes rttr) {
        log.info("contact by " + company + "/" + tno);
        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/survey";
        }

        model.addAttribute("company", company);
        model.addAttribute("tno", tno);
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("companyInfo", origin);
        });

        // mbo turn에 따른 navbar 구분을 위해
        turnService.read(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
        });

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];
        String redirectPath = pathInfo + "/contact";
        return redirectPath;
    }

    /**
     * 문의사항 등록 함수
     * 
     * @param company  회사 이름
     * @param tno      회차 id
     * @param helpDesk 문의 내용 객체
     * @param request  요청 정보 객체
     * @param rttr     재전송 정보
     * @return 문의 페이지
     */
    @PostMapping(value = { "/survey/help", "/mbo/help" })
    public String userContact(String company, long tno, HelpDesk helpDesk, HttpServletRequest request,
            RedirectAttributes rttr) {
        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        helpDesk.setSurveyInfo(pathInfo);
        helpDeskService.register(helpDesk);

        rttr.addAttribute("company", company);
        rttr.addAttribute("tno", tno);
        rttr.addFlashAttribute("msg", "register");

        String redirectPath = "redirect:/" + pathInfo + "/contact";
        return redirectPath;
    }
}