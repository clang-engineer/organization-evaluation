package com.evaluation.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.evaluation.domain.Turn;
import com.evaluation.service.TurnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <code>TurnController</code>객체는 회차 정보를 REST로 관리한다.
 */
@RestController
@RequestMapping("/turns/*")
@Slf4j
public class TurnController {

	@Autowired
	private TurnService turnService;

	/**
	 * 회차를 등록한다.
	 * 
	 * @param cno  회사 id
	 * @param turn 회차 정보
	 * @return 회차 목록 + 상태
	 */
	@Transactional
	@PostMapping("/{cno}")
	public ResponseEntity<Optional<List<Turn>>> register(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("controller : addTurn " + turn);

		turn.setCno(cno);
		turnService.register(turn);

		return new ResponseEntity<>(getTurnList(cno), HttpStatus.CREATED);
	}

	/**
	 * 회차를 수정한다.
	 * 
	 * @param cno  회사 id
	 * @param turn 회차 정보
	 * @return 회차 목록 + 상태
	 */
	@Transactional
	@PutMapping("/{cno}")
	public ResponseEntity<Optional<List<Turn>>> modify(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("controller : modfify turn " + turn);

		// 기본 정보 초기화 되는 것 때문에, 아래의 수식을 추가함.
		turnService.read(turn.getTno()).map(Turn::getInfoSurvey).ifPresent(origin -> {
			turn.setInfoSurvey(origin);
		});
		turnService.read(turn.getTno()).map(Turn::getInfoMbo).ifPresent(origin -> {
			turn.setInfoMbo(origin);
		});

		turnService.modify(turn);

		return new ResponseEntity<Optional<List<Turn>>>(getTurnList(cno), HttpStatus.CREATED);
	}

	/**
	 * 회차를 삭제한다.
	 * 
	 * @param cno 회사 id
	 * @param tno 회차 id
	 * @return 회차 목록 + 상태
	 */
	@Transactional
	@DeleteMapping("/{cno}/{tno}")
	public ResponseEntity<Optional<List<Turn>>> remove(@PathVariable("cno") Long cno, @PathVariable("tno") Long tno) {
		log.info("controller : remove Turn " + tno);

		turnService.remove(tno);

		return new ResponseEntity<Optional<List<Turn>>>(getTurnList(cno), HttpStatus.OK);
	}

	/**
	 * 한 회사에 속한 모든 회차 목록을 읽는다.
	 * 
	 * @param cno 회사 id
	 * @return 회차 목록 + 상태
	 */
	@GetMapping("/{cno}")
	public ResponseEntity<Optional<List<Turn>>> getTurnByCompany(@PathVariable("cno") Long cno) {
		log.info("controller : get all turns by " + cno);

		return new ResponseEntity<Optional<List<Turn>>>(getTurnList(cno), HttpStatus.OK);
	}

	/**
	 * 회차 목록을 읽는 함수
	 * 
	 * @param cno 회사 id
	 * @return 회차 목록
	 */
	private Optional<List<Turn>> getTurnList(Long cno) {
		log.info("getTurnList" + cno);

		return turnService.getTurnsOfCompany(cno);
	}
}
