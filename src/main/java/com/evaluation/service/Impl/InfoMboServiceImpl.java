package com.evaluation.service.Impl;

import com.evaluation.domain.InfoMbo;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.InfoMboService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InfoMboServiceImpl implements InfoMboService {

	@Setter(onMethod_ = { @Autowired })
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
		log.info("service : info360 get " + tno);

		return turnRepo.findById(tno).get().getInfoMbo();
	}

	@Override
	public void modify(Long tno, InfoMbo infoMbo) {
		log.info("service : info360 modify " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMbo(infoMbo);
			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : info360 remove " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMbo(null);
			turnRepo.save(origin);
		});

	}

}
