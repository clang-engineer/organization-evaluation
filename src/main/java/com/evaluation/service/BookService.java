package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Book;

public interface BookService {
	void register(Book book);

	Optional<Book> read(int bno);

	void modify(Book book);

	void remove(int bno);

	Optional<List<Book>> findAll();

	Optional<List<Book>> findByType(String type);
}
