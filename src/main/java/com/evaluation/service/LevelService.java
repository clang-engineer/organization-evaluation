package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Level;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>LevelService</code> 객체는 Level객체 sevice 계층의 interface를 표현한다.
 */
public interface LevelService {

	/**
	 * 직급 정보를 등록한다.
	 * 
	 * @param level 직급 정보
	 */
	void register(Level level);

	/**
	 * 직급 정보를 불러온다.
	 * 
	 * @param lno 직급 id
	 * @return 직급 정보
	 */
	Optional<Level> read(long lno);

	/**
	 * 직급 정보를 수정한다.
	 * 
	 * @param level 직급 정보
	 */
	void modify(Level level);

	/**
	 * 직급 정보를 삭제한다.
	 * 
	 * @param lno 직급 id
	 */
	void remove(long lno);

	/**
	 * 한 회사의 모든 직급 정보를 불러온다.
	 * 
	 * @param cno 회사 id
	 * @param vo  페이지 정보
	 * @return 페이징 처리된 직급 정보 리스트
	 */
	Page<Level> getList(long cno, PageVO vo);
}
