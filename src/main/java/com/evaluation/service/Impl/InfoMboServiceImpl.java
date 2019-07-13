package com.evaluation.service.Impl;

import com.evaluation.domain.Turn;
import com.evaluation.domain.embeddable.InfoMbo;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.InfoMboService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InfoMboServiceImpl implements InfoMboService {

	@Autowired
	private TurnRepository turnRepo;

	@Override
	public void register(Long tno, InfoMbo infoMbo) {

		log.info("service : infoMbo register " + infoMbo);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMbo(infoMbo);
			turnRepo.save(origin);
		});
	}

	@Override
	public InfoMbo read(long tno) {
		log.info("service : infoMbo get " + tno);

		return turnRepo.findById(tno).map(Turn::getInfoMbo).orElse(null);
	}

	@Override
	public void modify(Long tno, InfoMbo infoMbo) {
		log.info("service : infoMbo modify " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMbo(infoMbo);
			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : infoMbo remove " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMbo(null);
			turnRepo.save(origin);
		});

	}

}