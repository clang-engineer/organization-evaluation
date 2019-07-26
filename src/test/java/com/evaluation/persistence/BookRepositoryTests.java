package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.evaluation.domain.Book;
import com.evaluation.domain.embeddable.Content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class BookRepositoryTests {

	@Autowired
	BookRepository repo;

	public static Book book;

	@Test
	public void testtDI() {
		assertNotNull(repo);
	}

	@Test
	public void testInert360() {

		Book book = new Book();
		book.setTitle("매우 긍정 - 매우 부정");
		List<Content> contents = new ArrayList<Content>();
		contents.add(new Content("매우 그렇지 않다", 1));
		contents.add(new Content("그렇지 않다", 2));
		contents.add(new Content("보통이다", 3));
		contents.add(new Content("그렇다", 4));
		contents.add(new Content("매우 그렇다", 5));

		book.setContents(contents);
		book.setType("360Reply");
		repo.save(book);

		Book book2 = new Book();
		book2.setTitle("아주 긍정 - 아주 부정");
		List<Content> contents2 = new ArrayList<Content>();
		contents2.clear();
		contents2.add(new Content("아주 그렇지 않다", 1));
		contents2.add(new Content("그렇지 않다", 2));
		contents2.add(new Content("보통이다", 3));
		contents2.add(new Content("그렇다", 4));
		contents2.add(new Content("아주 그렇다", 5));

		book2.setContents(contents2);
		book2.setType("360Reply");
		repo.save(book2);

		Book book3 = new Book();
		book3.setTitle("매우 우수하다 - 매우 부족하다");
		List<Content> contents3 = new ArrayList<Content>();
		contents3.clear();
		contents3.add(new Content("매우 부족하다", 1));
		contents3.add(new Content("부족하다", 2));
		contents3.add(new Content("보통이다", 3));
		contents3.add(new Content("우수", 4));
		contents3.add(new Content("매우 우수하다", 5));

		book3.setContents(contents3);
		book3.setType("360Reply");
		repo.save(book3);

	}

	@Test
	public void testInertMbo() {

		Book book = new Book();
		book.setTitle("High - Low");
		List<Content> contents = new ArrayList<Content>();
		contents.add(new Content("Excellent", 1.2));
		contents.add(new Content("Good", 1.1));
		contents.add(new Content("Satisfactory", 1));
		contents.add(new Content("Unsatisfactory", 0.9));
		contents.add(new Content("Poor", 0.8));

		book.setContents(contents);
		book.setType("MboReply");
		repo.save(book);

		Book book2 = new Book();
		book2.setTitle("A - E");
		List<Content> contents2 = new ArrayList<Content>();
		contents2.add(new Content("A", 1.2));
		contents2.add(new Content("B", 1.1));
		contents2.add(new Content("C", 1));
		contents2.add(new Content("D", 0.9));
		contents2.add(new Content("E", 0.8));

		book2.setContents(contents2);
		book2.setType("MboReply");
		repo.save(book2);

		Book book3 = new Book();
		book3.setTitle("weight H - L");
		List<Content> contents3 = new ArrayList<Content>();
		contents3.add(new Content("High", 1.1));
		contents3.add(new Content("Middle", 1));
		contents3.add(new Content("Low", 0.9));

		book3.setContents(contents3);
		book3.setType("MboReply");
		repo.save(book3);
	}

	@Test
	public void testRead() {
		repo.findById(1);
	}

	@Test
	public void testUpdate() {

		repo.findById(1).ifPresent(origin -> {
			origin.setTitle("modified");
			repo.save(origin);
		});
	}

	@Test
	public void testDelete() {
		repo.deleteById(1);
	}

	@Test
	public void testRemoveContents() {
		repo.findById(1).ifPresent(origin -> {
			origin.getContents().remove(1);
			repo.save(origin);
		});
	}

	@Test
	public void testFindAll() {
		Sort sort = new Sort(Sort.Direction.ASC, "bno");
		repo.findAll(sort).ifPresent(origin -> {
			origin.forEach(book -> log.info("" + book.getBno()));
		});
		;
	}

	@Test
	public void testFindByType() {
		log.info("" + repo.findByType("360Reply"));
	}
}
