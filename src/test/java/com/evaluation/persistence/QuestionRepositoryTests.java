package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import com.evaluation.domain.Question;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class QuestionRepositoryTests {

	@Autowired
	QuestionRepository repo;

	@Test
	public void testDI() {
		assertNotNull(repo);
	}

	@Before
	public void testInsert() {

		Long[] arr = { 1L, 2L, 3L };

		Arrays.stream(arr).forEach(num -> {
			IntStream.range(1, 31).forEach(i -> {
				Question question = new Question();
				question.setDivision1("division1" + i);
				question.setDivision2("division2" + i);
				question.setIdx(i);
				question.setCategory("category" + i);
				question.setItem("question" + i);
				question.setTno(1L);
				repo.save(question);
			});
		});
	}

	@Test
	public void testDeleteByTno() {
		repo.deleteByTno(1L);
	}

	@Test
	public void testGetListByDivision() {
		repo.getListByDivision(1L, "division11", "division21").ifPresent(list -> log.info("" + list));
	}

	@Test
	public void testGetDistinctDivisionCountByTno() {
		repo.getDistinctDivisionCountByTno(1L).ifPresent(origin -> {
			log.info("" + origin);
		});
	}

	@Test
	public void testFindByTno() {
		repo.findByTno(1L).ifPresent(origin -> {
			origin.forEach(question -> {
				log.info("" + question);
			});
		});
	}

	@Test
	public void testGetListCategory() {
		repo.getListCategory(1L).forEach(question -> {
			log.info("" + question);
		});
	}

	@Test
	public void testList1() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "qno");
		Page<Question> result = repo.findAll(repo.makePredicate(null, null, 2L), pageable);
		log.info("PAGE : " + result.getPageable());

		result.getContent().forEach(question -> log.info("" + question));
	}

	@Test
	public void testList2() {

		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "qno");
		Page<Question> result = repo.findAll(repo.makePredicate("division", "11", 1L), pageable);
		log.info("PAGE : " + result.getPageable());

		result.getContent().forEach(question -> log.info("" + question.getQno()));

	}

}