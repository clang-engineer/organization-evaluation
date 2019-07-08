package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.evaluation.domain.Book;

/**
 * <code>BookRepository</code> 객체는 Book 객체의 영속화를 위해 표현한다.
 */
public interface BookRepository extends CrudRepository<Book, Integer> {

	/**
	 * 전체 Book list를 불러온다.
	 * 
	 * @param sort 정렬 지정.
	 * @return book 리스트
	 */
	Optional<List<Book>> findAll(Sort sort);

	/**
	 * 전체 Book list를 불러온다.
	 * 
	 * @param type book의 type 지정(survey or mbo).
	 * @return book 리스트
	 */
	Optional<List<Book>> findByType(String type);
}
