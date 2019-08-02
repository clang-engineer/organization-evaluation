package com.evaluation.controller;

import com.evaluation.domain.Level;
import com.evaluation.service.LevelService;
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
 * <code>LevelController 직급 정보를 관리한다.</code>
 */
@Controller
@RequestMapping("/level/*")
@Slf4j
public class LevelController {

    @Autowired
    LevelService levelService;

    @Autowired
    TurnService turnService;

    /**
     * 직급 정보를 등록한다.
     * 
     * @param tno   회차 id
     * @param level 직급 정보
     * @param rttr  재전송 정보
     * @return 직급 목록 페이지
     */
    @PostMapping("/register")
    public String register(long tno, Level level, RedirectAttributes rttr) {
        log.info("level register by " + tno + level);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        turnService.read(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            level.setCno(cno);
            levelService.register(level);
        });

        return "redirect:/level/list";
    }

    /**
     * 직급 정보를 수정한다.
     * 
     * @param tno   회차 id
     * @param level 직급 정보
     * @param vo    페이지 정보
     * @param rttr  재전송 정보
     * @return 직급 목록 페이지
     */
    @PostMapping("/modify")
    public String modify(long tno, Level level, PageVO vo, RedirectAttributes rttr) {
        log.info("modify " + level);

        levelService.modify(level);

        rttr.addFlashAttribute("msg", "modify");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/level/list";
    }

    /**
     * 직급 정보를 삭제한다.
     * 
     * @param tno  회차 id
     * @param lno  직급 id
     * @param vo   페이지 정보
     * @param rttr 재전송 정보
     * @return 직급 목록 페이지
     */
    @PostMapping("/remove")
    public String remove(long tno, long lno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + lno);

        levelService.remove(lno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/level/list";
    }

    /**
     * 직급 목록을 읽어온다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("level list by " + tno);

        turnService.read(tno).ifPresent(origin->{
            model.addAttribute("turn", origin);
            
            Page<Level> result = levelService.getList(origin.getCno(), vo);
            model.addAttribute("result", new PageMaker<>(result));
        });

    }

}
