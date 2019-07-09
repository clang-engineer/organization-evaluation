package com.evaluation.service;

import com.evaluation.domain.embeddable.InfoMbo;

/**
 * <code>InfoMboService</code> 객체는 InfoMbo객체 sevice 계층의 interface를 표현한다.
 */
public interface InfoMboService {
	/**
	 * 한 회차의 Mbo정보를 등록한다.
	 * 
	 * @param tno     회차 id
	 * @param infoMbo Mbo정보
	 */
	void register(Long tno, InfoMbo infoMbo);

	/**
	 * 한 회차의 Mbo정보를 불러=온다.
	 * 
	 * @param tno 회차 id
	 * @return Mbo 정보
	 */
	InfoMbo read(long tno);

	/**
	 * 한 회차의 Mbo정보를 수정한다.
	 * 
	 * @param tno     회차 id
	 * @param infoMbo Mbo정보
	 */
	void modify(Long tno, InfoMbo infoMbo);

	/**
	 * 한 회차의 Mbo정보를 삭제한다.
	 * 
	 * @param tno 회차 id
	 */
	void remove(long tno);
}
