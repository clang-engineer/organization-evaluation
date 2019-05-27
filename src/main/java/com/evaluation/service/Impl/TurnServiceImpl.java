package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.TurnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TurnServiceImpl implements TurnService {

	@Setter(onMethod_ = { @Autowired })
	private TurnRepository turnRepo;

	@Transactional
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

		turnRepo.findById(turn.getTno()).ifPresent(origin -> {
			origin.setTitle(turn.getTitle());
			origin.setTypes(turn.getTypes());
			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : turn remove " + tno);
		turnRepo.deleteById(tno);
	}

	@Override
	public List<Turn> getList(Long cno) {
		log.info("service : turn getList by " + cno);
		List<Turn> result = turnRepo.getTurnsOfCompany(cno);
		return result;
	}

}
