package com.evaluation.service;

import com.evaluation.domain.InfoSurvey;

public interface InfoSurveyService {
	public void register(Long tno, InfoSurvey infoSurvey);

	public InfoSurvey read(long tno);

	public void modify(Long tno, InfoSurvey infoSurvey);

	public void remove(long tno);
}
