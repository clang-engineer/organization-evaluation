package com.evaluation.controller;

import com.evaluation.domain.InfoSurvey;
import com.evaluation.service.InfoSurveyService;

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
@RequestMapping("/info360/*")
@Slf4j
public class Info360Controller {

	@Setter(onMethod_ = { @Autowired })
	private InfoSurveyService info360Service;

	@GetMapping("/read")
	public void view(long tno, Model model) {
		log.info("info360 read get " + tno);

		model.addAttribute("tno", tno);
		model.addAttribute("info360", info360Service.read(tno));
	}

	@GetMapping("/modify")
	public void modify(long tno, Model model) {
		log.info("info360 modify get" + tno);

		model.addAttribute("tno", tno);
		model.addAttribute("info360", info360Service.read(tno));
	}

	@PostMapping("/modify")
	public String modify(long tno, InfoSurvey info360, RedirectAttributes rttr) {
		log.info("controller : info360 modify post " + info360);

		info360Service.modify(tno, info360);

		rttr.addAttribute("tno", tno);
		return "redirect:/info360/view";
	}
}
