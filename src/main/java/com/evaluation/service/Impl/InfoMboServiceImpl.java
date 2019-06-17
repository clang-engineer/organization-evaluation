package com.evaluation.service.Impl;

import com.evaluation.domain.InfoMBO;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.InfoMBOService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InfoMBOServiceImpl implements InfoMBOService {

	@Setter(onMethod_ = { @Autowired })
	private TurnRepository turnRepo;

	@Override
	public void register(Long tno, InfoMBO infoMBO) {

		log.info("service : infoMBO register " + infoMBO);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMBO(infoMBO);
			turnRepo.save(origin);
		});
	}

	@Override
	public InfoMBO read(long tno) {
		log.info("service : infoMBO get " + tno);

		return turnRepo.findById(tno).get().getInfoMBO();
	}

	@Override
	public void modify(Long tno, InfoMBO infoMBO) {
		log.info("service : infoMBO modify " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMBO(infoMBO);
			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : infoMBO remove " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoMBO(null);
			turnRepo.save(origin);
		});

	}

}