package com.evaluation.controller;

import com.evaluation.domain.HelpDesk;
import com.evaluation.service.HelpDeskService;
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
 * <code>HelpDeskController</code>객체는 문의 정보를 관리한다.
 */
@Controller
@RequestMapping("/help/*")
@Slf4j
public class HelpDeskController {

    @Autowired
    private HelpDeskService helpDeskService;

    /**
     * 문의 목록을 관리한다.
     * 
     * @param vo    페이저 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void list(PageVO vo, Model model) {
        log.info("controller : help lisit by " + vo);
        Page<HelpDesk> result = helpDeskService.getList(vo);
        model.addAttribute("result", new PageMaker<>(result));
    }

    /**
     * 문의 등록 화면을 표현한다.
     */
    @GetMapping("/register")
    public void register() {
        log.info("controller : help register get");
    }

    /**
     * 문의를 등록한다.
     * 
     * @param helpDesk 문의 정보
     * @param rttr     재전송 정보
     * @return 문의 목록
     */
    @PostMapping("/register")
    public String register(HelpDesk helpDesk, RedirectAttributes rttr) {
        log.info("controller : help register" + helpDesk);
        helpDeskService.register(helpDesk);
        
        rttr.addFlashAttribute("msg", "register");

        return "redirect:/help/list";
    }

    /**
     * 문의 내용을 읽어온다.
     * 
     * @param hno   문의 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/read")
    public void read(long hno, PageVO vo, Model model) {
        log.info("controller : help read by " + hno);

        model.addAttribute("pageVO", vo);
        helpDeskService.read(hno).ifPresent(origin -> {
            model.addAttribute("help", origin);
        });
    }

    /**
     * 문의 수정 화면을 읽어온다.
     * 
     * @param hno   문의 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/modify")
    public void modify(long hno, PageVO vo, Model model) {
        log.info("controller : help modify by " + hno);

        model.addAttribute("pageVO", vo);
        helpDeskService.read(hno).ifPresent(origin -> {
            model.addAttribute("help", origin);
        });
    }

    /**
     * 문의를 수정한다.
     * 
     * @param helpDesk 문의 정보
     * @param vo       페이지 정보
     * @param rttr     재전송 정보
     * @return 문의 화면
     */
    @PostMapping("/modify")
    public String modify(HelpDesk helpDesk, PageVO vo, RedirectAttributes rttr) {
        log.info("controller : help modify by " + helpDesk);

        helpDeskService.modify(helpDesk);

        rttr.addFlashAttribute("msg", "modify");
        rttr.addAttribute("hno", helpDesk.getHno());

        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/help/read";
    }

    /**
     * 문의를 삭제한다.
     * 
     * @param hno  문의 id
     * @param vo   페이지 정보
     * @param rttr 재전송 정보
     * @return 문의 목록
     */
    @PostMapping("/remove")
    public String remove(Long hno, PageVO vo, RedirectAttributes rttr) {
        log.info("controller : help remove by " + hno);

        helpDeskService.remove(hno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/help/list";
    }
}