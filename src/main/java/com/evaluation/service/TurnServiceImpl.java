package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;
import com.evaluation.persistence.TurnRepository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TurnServiceImpl implements TurnService {

	@Setter(onMethod_ = { @Autowired })
	private TurnRepository turnRepo;

	@Override
	public void register(Turn turn) {
		log.info("service : turn register " + turn);
		turnRepo.save(turn);
	}

	@Override
	public Optional<Turn> get(long tno) {
		log.info("service : turn get " + tno);
		return turnRepo.findById(tno);
	}

	@Override
	public void modify(Turn turn) {
		log.info("service : turn modify " + turn);
		turnRepo.save(turn);
	}

	@Override
	public void remove(long tno) {
		log.info("service : turn remove " + tno);
		turnRepo.deleteById(tno);
	}

	@Override
	public List<Turn> getList(Company company) {
		log.info("service : turn getList by " + company);
		List<Turn> result = turnRepo.getTurnsOfCompany(company);
		return result;
	}

}
