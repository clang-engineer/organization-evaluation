package com.evaluation.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.evaluation.domain.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

	List<Book> findAll(Sort sort);

	List<Book> findByType(String type);
}
