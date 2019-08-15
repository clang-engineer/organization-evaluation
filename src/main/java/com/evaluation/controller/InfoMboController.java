package com.evaluation.controller;

import com.evaluation.domain.embeddable.InfoMbo;
import com.evaluation.service.BookService;
import com.evaluation.service.TurnService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <code>InfoMboController</code>객체는 Mbo 설정 정보를 관리한다.
 */
@Controller
@RequestMapping("/turns/{tno}")
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
	@GetMapping("/infoMbo")
	public String view(@PathVariable("tno") long tno, Model model) {
		log.info("infoMbo read get " + tno);

		turnService.read(tno).ifPresent(origin -> {
			model.addAttribute("turn", origin);
		});

		bookService.findByType("MboReply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});

		return "infoMbo/read";
	}

	/**
	 * Mbo 설정 정보를 수정한다.
	 * 
	 * @param tno     회차 id
	 * @param infoMbo Mbo 설정 정보
	 * @return Mbo 설정 정보
	 */
	@PutMapping("/infoMbo")
	public ResponseEntity<HttpStatus> modify(@PathVariable("tno") long tno, @RequestBody InfoMbo infoMbo) {
		log.info("controller : infoMbo modify post " + infoMbo);

		turnService.read(tno).ifPresent(origin -> {
			origin.setInfoMbo(infoMbo);
			turnService.register(origin);
		});

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
