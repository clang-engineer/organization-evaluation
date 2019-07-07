package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.evaluation.domain.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

	//book은 페이지 없이 전부 정렬만해서 list보여줌
	Optional<List<Book>> findAll(Sort sort);

	//survey, mbo info등록할 때 회답유형 선택하기 위해
	Optional<List<Book>> findByType(String type);
}
