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
import com.evaluation.service.TurnService;

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
		log.info("addReply.......................");

		Company company = new Company();
		company.setCno(cno);
		turn.setCompany(company);

		turnService.register(turn);
		return new ResponseEntity<>(getTurnList(company), HttpStatus.CREATED);
	}

	@Transactional
	@PutMapping("/{cno}")
	public ResponseEntity<List<Turn>> modify(@PathVariable("cno") Long cno, @RequestBody Turn turn) {
		log.info("modfify turn : " + turn);

		turnService.get(turn.getTno()).ifPresent(origin -> {
			origin.setTitle(turn.getTitle());
			origin.setType(turn.getType());
			turnService.modify(origin);
		});

		Company company = new Company();
		company.setCno(cno);
		return new ResponseEntity<List<Turn>>(getTurnList(company), HttpStatus.CREATED);
	}

	@Transactional
	@DeleteMapping("/{cno}/{tno}")
	public ResponseEntity<List<Turn>> remove(@PathVariable("cno") Long cno, @PathVariable("tno") Long tno) {
		log.info("delete Turn : " + tno);

		turnService.remove(tno);

		Company company = new Company();
		company.setCno(cno);
		return new ResponseEntity<List<Turn>>(getTurnList(company), HttpStatus.OK);
	}

	@GetMapping("/{cno}")
	public ResponseEntity<List<Turn>> getTurnByCompany(@PathVariable("cno") Long cno) {
		log.info("get all turns...");

		Company company = new Company();
		company.setCno(cno);
		return new ResponseEntity<List<Turn>>(getTurnList(company), HttpStatus.OK);
	}

	private List<Turn> getTurnList(Company company) throws RuntimeException {
		log.info("getTurnsByCompany..." + company);

		return turnService.getList(company);
	}
}
