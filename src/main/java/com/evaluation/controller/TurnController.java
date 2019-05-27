package com.evaluation.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.evaluation.domain.Turn;
import com.evaluation.persistence.TurnRepository;
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

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/turns/*")
@Slf4j
public class TurnController {

	@Setter(onMethod_ = @Autowired)
	private TurnRepository turnRepo;

	@Setter(onMethod_ = @Autowired)
	private TurnService turnService;

	@Transactional
	@PostMapping("/{cno}")
	public ResponseEntity<List<Turn>> addTurn(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("controller : addTurn " + turn);

		turn.setCno(cno);

		turnService.register(turn);

		return new ResponseEntity<>(getTurnList(cno), HttpStatus.CREATED);
	}

	@Transactional
	@PutMapping("/{cno}")
	public ResponseEntity<List<Turn>> modify(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("controller : modfify turn " + turn);

		turnService.get(turn.getTno()).ifPresent(origin -> {
			origin.setTitle(turn.getTitle());
			origin.setTypes(turn.getTypes());
			turnService.modify(origin);
		});

		return new ResponseEntity<List<Turn>>(getTurnList(cno), HttpStatus.CREATED);
	}

	@Transactional
	@DeleteMapping("/{cno}/{tno}")
	public ResponseEntity<List<Turn>> remove(@PathVariable("cno") Long cno, @PathVariable("tno") Long tno) {
		log.info("controller : remove Turn " + tno);

		turnService.remove(tno);

		return new ResponseEntity<List<Turn>>(getTurnList(cno), HttpStatus.OK);
	}

	@GetMapping("/{cno}")
	public ResponseEntity<List<Turn>> getTurnByCompany(@PathVariable("cno") Long cno) {
		log.info("controller : get all turns by " + cno);

		return new ResponseEntity<List<Turn>>(getTurnList(cno), HttpStatus.OK);
	}

	private List<Turn> getTurnList(Long cno) throws RuntimeException {
		log.info("getTurnList" + cno);

		return turnService.getList(cno);
	}
}
