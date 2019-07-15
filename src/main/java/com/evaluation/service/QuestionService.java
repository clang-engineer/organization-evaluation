package com.evaluation.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Question;
import com.evaluation.vo.PageVO;

/**
 * <code>QuestionService</code> 객체는 Question객체 sevice 계층의 interface를 표현한다.
 */
public interface QuestionService {
	/**
	 * 질문 정보를 등록한다.
	 * 
	 * @param question 질문 정보
	 */
	void register(Question question);

	/**
	 * 질문 정보를 읽어온다.
	 * 
	 * @param qno 질문 id
	 * @return 질문 정보
	 */
	Optional<Question> read(Long qno);

	/**
	 * 질문 정보를 수정한다.
	 * 
	 * @param question 질문 정보
	 */
	void modify(Question question);

	/**
	 * 질문 정보를 삭제한다.
	 * 
	 * @param qno 질문 id
	 */
	void remove(Long qno);

	/**
	 * 한 회차의 질문 정보를 불러온다.
	 * 
	 * @param tno 회차 id
	 * @param vo  페이지 정보
	 * @return 페이징 처리 된 질문 리스트
	 */
	Page<Question> getList(long tno, PageVO vo);

	/**
	 * 한 회차의 모든 질문 정보를 삭제한다.
	 * 
	 * @param tno 회차 ids
	 */
	void deleteByTno(long tno);

	/**
	 * 한 회차의 직군 계층 별 문항수를 반환한다.
	 * 
	 * @param tno 회차 id
	 * @return 계층 별 문항수를 담은 리스트의 리스트
	 */
	Optional<List<List<String>>> getDistinctDivisionCountByTno(long tno);

	/**
	 * 한 회차의 직군 계층 별 질문 문항을 불러온다.
	 * 
	 * @param tno       회차 id
	 * @param division1 직군명
	 * @param division2 계층명
	 * @return 질문 정보 리스트의 리스트
	 */
	Optional<List<Question>> getListByDivision(long tno, String division1, String division2);

	/**
	 * 한 회차의 모든 질문 정보를 찾는다.
	 * 
	 * @param tno 회차 id
	 * @return 질문 정보 리스트
	 */
	Optional<List<Question>> findByTno(long tno);

	/**
	 * 한 회사 회차의 div1, div2, cate의 중복제거 정보리스트를 찾는다.
	 * 
	 * @param cno 회사 id
	 * @param tno 회차 id
	 * @return division1, division2, category 각각 중복제거 정보 Map
	 */
	Map<String, Object> getDistinctQuestionInfo(long cno, long tno);
}
