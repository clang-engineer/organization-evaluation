package com.evaluation.service.Impl;

import com.evaluation.domain.embeddable.InfoSurvey;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.InfoSurveyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InfoSurveyServiceImpl implements InfoSurveyService {

	@Autowired
	private TurnRepository turnRepo;

	@Override
	public void register(Long tno, InfoSurvey infoSurvey) {

		log.info("service : infoSurvey register " + infoSurvey);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoSurvey(infoSurvey);
			turnRepo.save(origin);
		});
	}

	@Override
	public InfoSurvey read(long tno) {
		log.info("service : infoSurvey get " + tno);

		return turnRepo.findById(tno).get().getInfoSurvey();
	}

	@Override
	public void modify(Long tno, InfoSurvey infoSurvey) {
		log.info("service : infoSurvey modify " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoSurvey(infoSurvey);
			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : infoSurvey remove " + tno);

		turnRepo.findById(tno).ifPresent(origin -> {
			origin.setInfoSurvey(null);
			turnRepo.save(origin);
		});

	}

}
