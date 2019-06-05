package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Book;

public interface BookService {
	public void register(Book book);

	public Optional<Book> read(int bno);

	public void modify(Book book);

	public void remove(int bno);

	public Optional<List<Book>> list();

	public Optional<List<Book>> listFindByType(String type);
}
