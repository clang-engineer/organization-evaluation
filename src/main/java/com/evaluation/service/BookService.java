package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Book;

public interface BookService {
	public void register(Book book);

	public Optional<Book> read(Long bno);

	public void modify(Book book);

	public void remove(Long bno);

	public List<Book> list();

	public List<Book> listFindByType(String type);
}
