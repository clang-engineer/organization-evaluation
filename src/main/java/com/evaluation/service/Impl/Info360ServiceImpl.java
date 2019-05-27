package com.evaluation.service.Impl;

import com.evaluation.domain.InfoSurvey;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.InfoSurveyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Info360ServiceImpl implements InfoSurveyService {

	@Setter(onMethod_ = { @Autowired })
	private TurnRepository turnRepo;

	@Override
	public void register(Long tno, InfoSurvey infoSurvey) {

		log.info("service : info360 register " + infoSurvey);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfo360(infoSurvey);
			turnRepo.save(origin);
		});
	}

	@Override
	public InfoSurvey read(long tno) {
		log.info("service : info360 get " + tno);

		return turnRepo.findById(tno).get().getInfo360();
	}

	@Override
	public void modify(Long tno, InfoSurvey infoSurvey) {
		log.info("service : info360 modify " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfo360(infoSurvey);
			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : info360 remove " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfo360(null);
			turnRepo.save(origin);
		});

	}

}
