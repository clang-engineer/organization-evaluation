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

@Controller
@RequestMapping("/level/*")
@Slf4j
public class LevelController {

    @Autowired
    LevelService levelService;

    @Autowired
    TurnService turnService;

    @PostMapping("/register")
    public String register(long tno, Level level, RedirectAttributes rttr) {
        log.info("level register by " + tno + level);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCno();
        level.setCno(cno);

        levelService.register(level);

        return "redirect:/level/list";
    }

    @PostMapping("/modify")
    public String modify(Level level, long tno, PageVO vo, RedirectAttributes rttr) {
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

    @PostMapping("/remove")
    public String remove(long lno, long tno, PageVO vo, RedirectAttributes rttr) {
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

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("level list by " + tno);

        model.addAttribute("tno", tno);

        long cno = turnService.get(tno).get().getCno();
        Page<Level> result = levelService.getListWithPaging(cno, vo);
        model.addAttribute("result", new PageMaker<>(result));

    }

}
