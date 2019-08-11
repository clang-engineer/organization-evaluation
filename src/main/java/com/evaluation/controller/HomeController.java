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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param request 요청 객체
     * @return Mbo로그인 페이지
     */
    @GetMapping(value = { "/surveys/{company}", "/mbos/{company}" })
    public String userLogin(@PathVariable("company") String company, HttpServletRequest request, Model model) {
        log.info("user login by " + company);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        // 회사에 관한 정보 찾고
        companyService.findByCompanyId(company).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("company", origin);
            // 회사 cno로 turn정보를 찾는다.
            if (pathInfo.equals("surveys")) {
                turnService.getTurnsInSurvey(cno).ifPresent(turn -> {
                    model.addAttribute("turns", turn);
                });
            } else if (pathInfo.equals("mbos")) {
                turnService.getTurnsInMbo(cno).ifPresent(turn -> {
                    model.addAttribute("turns", turn);
                });
            }
        });

        return pathInfo.substring(0, pathInfo.length() - 1) + "/index";
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
    @PostMapping("/surveys/login")
    public String surveyLogin(String company, long tno, Staff staff, HttpServletRequest request,
            RedirectAttributes rttr) {
        log.info("user login by" + company + "/" + tno + "/" + staff);

        if (tno == 0) {
            rttr.addFlashAttribute("error", "tno");
            return "redirect:/surveys/" + company;
        }

        if (!relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).isPresent()) {
            rttr.addFlashAttribute("error", "email");
            return "redirect:/surveys/" + company;
        } else if (!relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).map(Staff::getPassword)
                .orElse(null).equals(staff.getPassword())) {
            rttr.addFlashAttribute("error", "password");
            return "redirect:/surveys/" + company;
        }

        relationSurveyService.findByEvaluatorEmail(tno, staff.getEmail()).ifPresent(evaluator -> {
            if (evaluator.getPassword().equals(staff.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("evaluator", evaluator);
                session.setAttribute("tno", tno);
            }
        });

        // 로그인 실패하면 메인으로 가도 세션없어서 로그인 폼으로 감! 패스워드 일치여부에 관계없이 메인으로 리다이렉트.
        return "redirect:/surveys/" + company + "/main";
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
    @PostMapping("/mbos/login")
    public String login(String company, long tno, Staff staff, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("user login by " + company + "/" + tno + "/" + staff);

        if (tno == 0) {
            rttr.addFlashAttribute("error", "tno");
            return "redirect:/mbos/" + company;
        }

        if (!relationMboService.findByEvaluatorEmail(tno, staff.getEmail()).isPresent()) {
            rttr.addFlashAttribute("error", "email");
            return "redirect:/mbos/" + company;
        } else if (!relationMboService.findByEvaluatorEmail(tno, staff.getEmail()).map(Staff::getPassword).orElse(null)
                .equals(staff.getPassword())) {
            rttr.addFlashAttribute("error", "password");
            return "redirect:/mbos/" + company;
        }

        relationMboService.findByEvaluatorEmail(tno, staff.getEmail()).ifPresent(evaluator -> {
            if (evaluator.getPassword().equals(staff.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("evaluator", evaluator);
                session.setAttribute("tno", tno);
            }
        });

        // 로그인 실패하면 메인으로 가도 세션없어서 로그인 폼으로 감! 패스워드 일치여부에 관계없이 메인으로 리다이렉트.
        return "redirect:/mbos/" + company + "/main";
    }

    /**
     * 로그아웃 처리를 한다.
     * 
     * @param company 회자 이름
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @return 로그인 페이지
     */
    @PostMapping(value = { "/surveys/logout", "/mbos/logout" })
    public String userLogOut(String company, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("log out!");

        HttpSession session = request.getSession();
        session.invalidate();

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        String redirectPath = "redirect:/" + pathInfo + "/" + company;
        return redirectPath;
    }

    /**
     * 사용자 정보를 읽어온다.
     * 
     * @param company 회사 이름
     * @param request 요청 정보 객체
     * @param model   화면 전달 정보
     */
    @GetMapping(value = { "/surveys/{company}/profile", "/mbos/{company}/profile" })
    public String userProfile(@PathVariable("company") String company, HttpServletRequest request, Model model) {
        log.info("profile by " + company);

        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null || session.getAttribute("tno") == null) {
            return "redirect:/" + pathInfo + "/" + company;
        }

        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("company", origin);
        });

        long tno = (Long) session.getAttribute("tno");
        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });

        return pathInfo.substring(0, pathInfo.length() - 1) + "/profile";
    }

    /**
     * 사용자 정보를 수정한다.
     * 
     * @param company 회사 이름
     * @param staff   직원 정보
     * @param request 요청 정보 객체
     * @return 상태 메시지
     */
    @PutMapping(value = { "/surveys/{company}/profile", "/mbos/{company}/profile" })
    public ResponseEntity<HttpStatus> userProfileModify(@PathVariable("company") String company,
            @RequestBody Staff staff, HttpServletRequest request) {
        staffService.findByEmail(staff.getEmail()).ifPresent(origin -> {
            origin.setPassword(staff.getPassword());
            origin.setTelephone(staff.getTelephone());
            staffService.modify(origin);

            HttpSession session = request.getSession();
            session.setAttribute("evaluator", origin);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 문의 사항 페이지
     * 
     * @param company 회사 이름
     * @param model   화면 전달 정보
     * @return 문의 사항 페이지
     */
    @GetMapping(value = { "/surveys/{company}/contact", "/mbos/{company}/contact" })
    public String userContact(@PathVariable("company") String company, HttpServletRequest request, Model model) {
        log.info("contact by " + company);
        HttpSession session = request.getSession();
        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        if (session.getAttribute("evaluator") == null || session.getAttribute("tno") == null) {
            return "redirect:/" + pathInfo + "/" + company;
        }

        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("company", origin);
        });

        long tno = (long) session.getAttribute("tno");
        // mbo turn에 따른 navbar 구분을 위해
        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });

        return pathInfo.substring(0, pathInfo.length() - 1) + "/contact";
    }

    /**
     * 문의사항 등록 함수
     * 
     * @param company  회사 이름
     * @param helpDesk 문의 내용 객체
     * @param request  요청 정보 객체
     * @param rttr     재전송 정보
     * @return 문의 페이지로 리다이렉트
     */
    @PostMapping(value = { "/surveys/help", "/mbos/help" })
    public String userContact(String company, HelpDesk helpDesk, HttpServletRequest request, RedirectAttributes rttr) {
        String whatYouCall = request.getServletPath();
        String[] words = whatYouCall.split("/");
        String pathInfo = words[1];

        helpDesk.setSurveyInfo(pathInfo.substring(0, pathInfo.length() - 1));
        helpDeskService.register(helpDesk);

        rttr.addFlashAttribute("msg", "register");

        String redirectPath = "redirect:/" + pathInfo + "/" + company + "/contact";
        return redirectPath;
    }
}