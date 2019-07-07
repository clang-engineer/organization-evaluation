package com.evaluation.controller;

import com.evaluation.domain.embeddable.InfoSurvey;
import com.evaluation.service.BookService;
import com.evaluation.service.InfoSurveyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/infoSurvey/*")
@Slf4j
public class InfoSurveyController {

	@Autowired
	private InfoSurveyService infoSurveyService;

	@Autowired
	private BookService bookService;

	@GetMapping("/read")
	public void view(long tno, Model model) {
		log.info("infoSurvey read get " + tno);

		model.addAttribute("tno", tno);
		bookService.findByType("360Reply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
		model.addAttribute("infoSurvey", infoSurveyService.read(tno));
	}

	@GetMapping("/modify")
	public void modify(long tno, Model model) {
		log.info("infoSurvey modify get" + tno);

		model.addAttribute("tno", tno);
		bookService.findByType("360Reply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
		model.addAttribute("infoSurvey", infoSurveyService.read(tno));
	}

	@PostMapping("/modify")
	public String modify(long tno, InfoSurvey infoSurvey, RedirectAttributes rttr) {
		log.info("controller : infoSurvey modify post " + infoSurvey);

		log.info("" + infoSurvey.getStartDate());
		infoSurveyService.modify(tno, infoSurvey);

		rttr.addAttribute("tno", tno);
		return "redirect:/infoSurvey/read";
	}
}
