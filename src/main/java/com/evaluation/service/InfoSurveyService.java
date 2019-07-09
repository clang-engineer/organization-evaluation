package com.evaluation.service;

import com.evaluation.domain.embeddable.InfoSurvey;

/**
 * <code>InfoSurveyService</code> 객체는 InfoSurvey객체 sevice 계층의 interface를 표현한다.
 */
public interface InfoSurveyService {
	/**
	 * 한 회차의 Survey정보를 등록한다.
	 * 
	 * @param tno        회차 id
	 * @param infoSurvey Survey정보
	 */
	void register(Long tno, InfoSurvey infoSurvey);

	/**
	 * 한 회차의 Survey정보를 불러=온다.
	 * 
	 * @param tno 회차 id
	 * @return Survey 정보
	 */
	InfoSurvey read(long tno);

	/**
	 * 한 회차의 Survey정보를 수정한다.
	 * 
	 * @param tno        회차 id
	 * @param infoSurvey Survey정보
	 */
	void modify(Long tno, InfoSurvey infoSurvey);

	/**
	 * 한 회차의 Survey정보를 삭제한다.
	 * 
	 * @param tno 회차 id
	 */
	void remove(long tno);
}
