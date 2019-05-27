package com.evaluation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evaluation.domain.Info360;
import com.evaluation.service.Info360Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/info360/*")
@Slf4j
public class Info360Controller {

	@Setter(onMethod_ = { @Autowired })
	private Info360Service info360Service;

	@GetMapping("/index")
	public void home(long tno, Model model) {
		log.info("controller : DetalPage view get " + tno);

		info360Service.get(tno).ifPresent(company -> model.addAttribute("info360", company));
		long cno = info360Service.get(tno).get().getTurn().getCno();
		model.addAttribute("tno", tno);
		model.addAttribute("cno", cno);
	}

	@GetMapping("/view")
	public void view(long tno, Model model) {
		log.info("controller : info360 view get " + tno);

		model.addAttribute("tno", tno);
		info360Service.get(tno).ifPresent(company -> model.addAttribute("info360", company));
	}

	@GetMapping("/modify")
	public void modify(long tno, Model model) {
		log.info("controller : info360 modify get" + tno);

		model.addAttribute("tno", tno);
		info360Service.get(tno).ifPresent(company -> model.addAttribute("info360", company));
	}

	@PostMapping("/modify")
	public String modify(Info360 info360, RedirectAttributes rttr) {
		log.info("controller : info360 modify post " + info360);

		info360Service.modify(info360);
		rttr.addAttribute("tno", info360.getTno());
		return "redirect:/info360/view";
	}
}
