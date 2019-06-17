package com.evaluation.controller;

import com.evaluation.domain.InfoMBO;
import com.evaluation.service.BookService;
import com.evaluation.service.InfoMBOService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/infoMBO/*")
@Slf4j
public class InfoMBOController {

	@Setter(onMethod_ = { @Autowired })
	private InfoMBOService infoMBOService;

	@Setter(onMethod_ = { @Autowired })
	private BookService bookService;

	@GetMapping("/read")
	public void view(long tno, Model model) {
		log.info("infoMBO read get " + tno);

		model.addAttribute("tno", tno);
		bookService.listFindByType("MBOReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
		model.addAttribute("infoMBO", infoMBOService.read(tno));
	}

	@GetMapping("/modify")
	public void modify(long tno, Model model) {
		log.info("infoMBO modify get" + tno);

		model.addAttribute("tno", tno);
		bookService.listFindByType("MBOReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
		model.addAttribute("infoMBO", infoMBOService.read(tno));
	}

	@PostMapping("/modify")
	public String modify(long tno, InfoMBO infoMBO, RedirectAttributes rttr) {
		log.info("controller : infoMBO modify post " + infoMBO);

		log.info("" + infoMBO.getStartDate());
		infoMBOService.modify(tno, infoMBO);

		rttr.addAttribute("tno", tno);
		return "redirect:/infoMBO/read";
	}
}
