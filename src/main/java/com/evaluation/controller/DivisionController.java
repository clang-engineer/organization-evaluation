package com.evaluation.controller;

import com.evaluation.domain.Division;
import com.evaluation.domain.Turn;
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

/**
 * * <code>DivisionController</code>객체는 직군, 계층 정보를 관리한다.
 */
@Controller
@RequestMapping("/division/*")
@Slf4j
public class DivisionController {

    @Autowired
    DivisionService divisionService;

    @Autowired
    TurnService turnService;

    /**
     * 계층 정보를 등록한다.
     * 
     * @param tno      회차 id
     * @param division 계층 정보
     * @param rttr     재전달 정보
     * @return 계층 목록 페이지
     */
    @PostMapping("/register")
    public String register(long tno, Division division, RedirectAttributes rttr) {
        log.info("division register by " + tno + division);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        turnService.read(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            division.setCno(cno);
            divisionService.register(division);
        });

        return "redirect:/division/list";
    }

    /**
     * 계층 정보를 수정한다.
     * 
     * @param tno      회차 id
     * @param division 계층 정보
     * @param vo       페이지 정보
     * @param rttr     재전송 정보
     * @return 계층 목록 페이지
     */
    @PostMapping("/modify")
    public String modify(long tno, Division division, PageVO vo, RedirectAttributes rttr) {
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

    /**
     * 계층 정보를 삭제한다.
     * 
     * @param tno  회차 id
     * @param dno  계층 id
     * @param vo   페이지 정보
     * @param rttr 재전송 정보
     * @return 계층 목록 페이지
     */
    @PostMapping("/remove")
    public String remove(long tno, long dno, PageVO vo, RedirectAttributes rttr) {
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

    /**
     * 계층 목록을 읽어온다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("division list by " + tno);

        model.addAttribute("tno", tno);

        long cno = turnService.read(tno).map(Turn::getCno).orElse(null);
        Page<Division> result = divisionService.getList(cno, vo);
        model.addAttribute("result", new PageMaker<>(result));

    }

}
