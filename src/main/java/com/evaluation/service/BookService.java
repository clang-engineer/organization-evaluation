package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Book;

/**
 * <code>BookService</code> 객체는 Book객체 sevice 계층의 interface를 표현한다.
 */
public interface BookService {
	/**
	 * 회답 정볼를 등록한다.
	 * 
	 * @param book 회답 정보
	 */
	void register(Book book);

	/**
	 * 회답 정보를 읽어온다.
	 * 
	 * @param bno book id
	 * @return 회답 정보
	 */
	Optional<Book> read(int bno);

	/**
	 * 회답 정보를 수정한다.
	 * 
	 * @param book 회답 정보
	 */
	void modify(Book book);

	/**
	 * 회답 정보를 삭제한다.
	 * 
	 * @param bno Book id
	 */
	void remove(int bno);

	/**
	 * 모든 회답을 반환한다.
	 * 
	 * @return 회답 정보 List
	 */
	Optional<List<Book>> findAll();

	/**
	 * type(survey, mboO)에 따른 회답리스트를ㄴ 반환한다.
	 * 
	 * @param type 회답 type
	 * @return 회답 정보 리스트
	 */
	Optional<List<Book>> findByType(String type);
}
