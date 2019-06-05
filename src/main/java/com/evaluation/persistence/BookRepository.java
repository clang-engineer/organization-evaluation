package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.evaluation.domain.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

	Optional<List<Book>> findAll(Sort sort);

	Optional<List<Book>> findByType(String type);
}
