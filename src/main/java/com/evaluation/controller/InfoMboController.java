package com.evaluation.controller;

import com.evaluation.domain.embeddable.InfoMbo;
import com.evaluation.service.BookService;
import com.evaluation.service.InfoMboService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/infoMbo/*")
@Slf4j
public class InfoMboController {

	@Autowired
	private InfoMboService infoMboService;

	@Autowired
	private BookService bookService;

	@GetMapping("/read")
	public void view(long tno, Model model) {
		log.info("infoMbo read get " + tno);

		model.addAttribute("tno", tno);
		bookService.findByType("MboReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
		model.addAttribute("infoMbo", infoMboService.read(tno));
	}

	@GetMapping("/modify")
	public void modify(long tno, Model model) {
		log.info("infoMBO modify get" + tno);

		model.addAttribute("tno", tno);
		bookService.findByType("MboReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
		model.addAttribute("infoMbo", infoMboService.read(tno));
	}

	@PostMapping("/modify")
	public String modify(long tno, InfoMbo infoMbo, RedirectAttributes rttr) {
		log.info("controller : infoMbo modify post " + infoMbo);

		log.info("" + infoMbo.getStartDate());
		infoMboService.modify(tno, infoMbo);

		rttr.addAttribute("tno", tno);
		return "redirect:/infoMbo/read";
	}
}
