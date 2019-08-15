package com.evaluation.controller;

import com.evaluation.domain.embeddable.InfoSurvey;
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
 * <code>InfoSurveyController</code>객체는 Survey 설정 정보를 관리한다.
 */
@Controller
@RequestMapping("/turns/{tno}")
@Slf4j
@AllArgsConstructor
public class InfoSurveyController {

	private BookService bookService;

	private TurnService turnService;

	/**
	 * Survey 설정 정보를 읽어온다.
	 * 
	 * @param tno   회차 id
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/infoSurvey")
	public String view(@PathVariable("tno") long tno, Model model) {
		log.info("infoSurvey read get " + tno);

		turnService.read(tno).ifPresent(origin -> {
			model.addAttribute("turn", origin);
		});

		bookService.findByType("360Reply").ifPresent(origin -> {
			model.addAttribute("bookReply", origin);
		});

		return "infoSurvey/read";
	}

	/**
	 * Survey 설정 정보를 수정한다.
	 * 
	 * @param tno        회차 id
	 * @param infoSurvey Survey 설정 정보
	 * @return Survey 설정 정보 페이지
	 */
	@PutMapping("/infoSurvey")
	public ResponseEntity<HttpStatus> modify(@PathVariable("tno") long tno, @RequestBody InfoSurvey infoSurvey) {
		log.info("controller : infoSurvey modify post " + infoSurvey);

		turnService.read(tno).ifPresent(origin -> {
			origin.setInfoSurvey(infoSurvey);
			turnService.register(origin);
		});

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
