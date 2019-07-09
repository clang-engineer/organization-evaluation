package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;

/**
 * <code>TurnService</code> 객체는 Turn객체 sevice 계층의 interface를 표현한다.
 */
public interface TurnService {

	/**
	 * 회차 정보를 등록한다.
	 * 
	 * @param turn 회차 정보
	 */
	void register(Turn turn);

	/**
	 * 회차 정보를 읽어온다.
	 * 
	 * @param tno 회차 id
	 * @return 회차 정보
	 */
	Optional<Turn> read(long tno);

	/**
	 * 회차 정보를 수정한다.
	 * 
	 * @param turn 회차 정보
	 */
	void modify(Turn turn);

	/**
	 * 회차 정보를 삭제한다.
	 * 
	 * @param tno 회차 id
	 */
	void remove(long tno);

	/**
	 * 한 회사의 모든 회차 정보를 찾는다.
	 * 
	 * @param cno 회사 id
	 * @return 회차 정보 리스트
	 */
	Optional<List<Turn>> getTurnsOfCompany(Long cno);

	/**
	 * 서베이 상태와 기간을 통해서 진행 중인 회차를 찾는다.
	 * 
	 * @param cno 회사 id
	 * @return 회차 정보 리스트
	 */
	Optional<List<Turn>> getTurnsInSurvey(Long cno);

	/**
	 * Mbo 상태와 기간을 통해서 진행 중인 회차를 찾는다.
	 * 
	 * @param cno 회사 id
	 * @return 회차 정보 리스트
	 */
	Optional<List<Turn>> getTurnsInMbo(Long cno);

	/**
	 * 주관식 정보를 등록한다.
	 * 
	 * @param turn 회차 정보
	 */
	void commentRegister(Turn turn);
}
