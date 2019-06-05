package com.evaluation.persistence;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import com.evaluation.domain.Book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Commit
@Transactional
public class BookRepositoryTests {

	@Autowired
	BookRepository bookRepo;

	@Test
	public void turnTests() {
		log.info("============");
		log.info("" + bookRepo);
	}

	@Test
	public void testInertBook() {

		Book book = new Book();
		book.setTitle("매우 긍정 - 매우 부정");
		List<String> contents = Arrays.asList("매우 그렇다", "그렇다", "보통이다", "그렇지 않다", "매우 그렇지 않다");
		book.setContents(contents);
		bookRepo.save(book);
		
		Book book2 = new Book();
		book2.setTitle("아주 그렇다 - 아주 그렇지 않다");
		contents = Arrays.asList("아주 그렇다", "그렇다", "보통이다", "그렇지 않다", "아주 그렇지 않다");
		book2.setContents(contents);
		bookRepo.save(book2);
	}

	@Test
	public void testRemoveContents() {
		Book book = bookRepo.findById(14).get();
		List<String> contents = book.getContents();
		contents.remove(1);
		book.setContents(contents);
		bookRepo.save(book);
	}

	@Test
	public void testfindByType() {
		log.info("" + bookRepo.findByType("360Status"));
	}
}
