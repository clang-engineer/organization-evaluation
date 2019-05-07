package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;
import com.evaluation.persistence.TurnRepository;

import lombok.Setter;

@Service
public class TurnServiceImpl implements TurnService {

	@Setter(onMethod_ = { @Autowired })
	private TurnRepository turnRepo;

	@Override
	public void register(Turn turn) {
		turnRepo.save(turn);
	}

	@Override
	public Optional<Turn> get(long tno) {
		return turnRepo.findById(tno);
	}

	@Override
	public void modify(Turn turn) {
		turnRepo.save(turn);
	}

	@Override
	public void remove(long tno) {
		turnRepo.deleteById(tno);
	}

	@Override
	public List<Turn> getList(Company company) {
		List<Turn> result = turnRepo.getTurnsOfCompany(company);
		return result;
	}

}
