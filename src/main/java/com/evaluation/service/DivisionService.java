package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Division;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>DivisionService</code> 객체는 Division객체 sevice 계층의 interface를 표현한다.
 */
public interface DivisionService {

	/**
	 * 구분 정보를 등록한다.
	 * 
	 * @param division 구분 정보
	 */
	void register(Division division);

	/**
	 * 구분 정보를 읽어온다.
	 * 
	 * @param dno 구분 id
	 * @return 구분 정보 객체
	 */
	Optional<Division> read(long dno);

	/**
	 * 굽누 정보를 수정한다.
	 * 
	 * @param division 구분 정보 객체
	 */
	void modify(Division division);

	/**
	 * 구분 정보를 삭제한다.
	 * 
	 * @param dno 구분 id
	 */
	void remove(long dno);

	/**
	 * 한 회사의 구분 정보 리스트를 불러온다.
	 * 
	 * @param cno 회사 id
	 * @param vo  페이지 정보
	 * @return 페이지 처리된 구분 정보
	 */
	Page<Division> getList(long cno, PageVO vo);
}
