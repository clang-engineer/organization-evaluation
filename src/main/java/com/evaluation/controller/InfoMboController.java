package com.evaluation.controller;

import com.evaluation.domain.embeddable.InfoMbo;
import com.evaluation.service.BookService;
import com.evaluation.service.TurnService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <code>InfoMboController</code>객체는 Mbo 설정 정보를 관리한다.
 */
@Controller
@RequestMapping("/infoMbo/*")
@Slf4j
@AllArgsConstructor
public class InfoMboController {

	private BookService bookService;

	private TurnService turnService;

	/**
	 * Mbo 설정 정보를 읽어온다.
	 * 
	 * @param tno   회차 id
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/read")
	public void view(long tno, Model model) {
		log.info("infoMbo read get " + tno);

		turnService.read(tno).ifPresent(origin->{
			model.addAttribute("turn", origin);
		});

		bookService.findByType("MboReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
	}

	/**
	 * Mbo 설정 정보 수정 페이지를 읽어온다.
	 * 
	 * @param tno   회차 id
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/modify")
	public void modify(long tno, Model model) {
		log.info("infoMBO modify get" + tno);

		turnService.read(tno).ifPresent(origin->{
			model.addAttribute("turn", origin);
		});

		bookService.findByType("MboReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});
	}

	/**
	 * Mbo 설정 정보를 수정한다.
	 * 
	 * @param tno     회차 id
	 * @param infoMbo Mbo 설정 정보
	 * @param rttr    재전송 정보
	 * @return Mbo 설정 정보
	 */
	@PostMapping("/modify")
	public String modify(long tno, InfoMbo infoMbo, RedirectAttributes rttr) {
		log.info("controller : infoMbo modify post " + infoMbo);

		turnService.read(tno).ifPresent(origin->{
			origin.setInfoMbo(infoMbo);
			turnService.register(origin);
		});

		rttr.addAttribute("tno", tno);
		return "redirect:/infoMbo/read";
	}
}
