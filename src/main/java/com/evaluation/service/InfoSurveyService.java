package com.evaluation.service;

import com.evaluation.domain.embeddable.InfoSurvey;

public interface InfoSurveyService {
	void register(Long tno, InfoSurvey infoSurvey);

	InfoSurvey read(long tno);

	void modify(Long tno, InfoSurvey infoSurvey);

	void remove(long tno);
}
