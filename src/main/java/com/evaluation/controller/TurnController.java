package com.evaluation.controller;

import java.util.List;

import javax.transaction.Transactional;

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

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;
import com.evaluation.persistence.TurnRepository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/turns/*")
@Slf4j
public class TurnController {

	@Setter(onMethod_ = @Autowired)
	private TurnRepository turnRepo;

	@Transactional
	@PostMapping("/{cno}")
	public ResponseEntity<List<Turn>> addTurn(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("addReply.......................");
		log.info("cno : " + cno);
		log.info("Reply : " + turn);

		Company company = new Company();
		company.setCno(cno);

		turn.setCompany(company);
		turnRepo.save(turn);

		return new ResponseEntity<>(turnRepo.getTurnsOfCompany(company), HttpStatus.CREATED);
	}

	@Transactional
	@DeleteMapping("/{cno}/{tno}")
	public ResponseEntity<List<Turn>> remove(@PathVariable("cno") Long cno, @PathVariable("tno") Long tno) {
		log.info("delete Turn : " + tno);
		turnRepo.deleteById(tno);

		Company company = new Company();
		company.setCno(cno);
		return new ResponseEntity<List<Turn>>(turnRepo.getTurnsOfCompany(company), HttpStatus.OK);
	}

	@Transactional
	@PutMapping("/{cno}")
	public ResponseEntity<List<Turn>> modify(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("modfify turn : " + turn);

		turnRepo.findById(turn.getTno()).ifPresent(origin -> {
			origin.setTitle(turn.getTitle());
			origin.setSurveyType(turn.getSurveyType());
			turnRepo.save(origin);
		});

		Company company = new Company();
		company.setCno(cno);

		return new ResponseEntity<List<Turn>>(turnRepo.getTurnsOfCompany(company), HttpStatus.CREATED);
	}

	@GetMapping("/{cno}")
	public ResponseEntity<List<Turn>> getTurns(@PathVariable("cno") Long cno) {
		log.info("get all turns...");

		Company company = new Company();
		company.setCno(cno);
		return new ResponseEntity<List<Turn>>(turnRepo.getTurnsOfCompany(company), HttpStatus.OK);
	}
}
