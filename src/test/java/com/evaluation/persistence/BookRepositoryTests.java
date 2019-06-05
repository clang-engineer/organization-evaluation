package com.evaluation.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Book;

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

	// @Test
	public void testInertBook() {

		IntStream.range(1, 11).forEach(i -> {
			Book book = new Book();
			book.setTitle("test" + i + 1);

			List<String> contents = Arrays.asList("매우 그렇다" + i + 1, "그렇다" + i + 1, "보통이다" + i + 1, "그렇지 않다" + i + 1,
					"매우 그렇지 않다" + i + 1);
			book.setContents(contents);
			bookRepo.save(book);
		});
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
