package com.evaluation.persistence;

import java.util.Arrays;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import com.evaluation.domain.Question;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Commit
@Transactional
public class QuestionRepositoryTests {

	@Autowired
	QuestionRepository questionRepo;

	@Test
	public void QuestionTests() {
		log.info("============");
		log.info("" + questionRepo);
	}

	@Test
	public void testInertQuestion() {

		Long[] arr = { 1L, 2L, 3L };

		Arrays.stream(arr).forEach(num -> {
			IntStream.range(1, 31).forEach(i -> {
				Question question = new Question();
				question.setDivision1("division1" + i);
				question.setDivision2("division2" + i);
				question.setIdx("" + i);
				question.setCategory("category" + i);
				question.setItem("question" + i);
				question.setTno(num);
				questionRepo.save(question);
			});
		});
	}

	@Test
	public void testList1() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "qno");
		Page<Question> result = questionRepo.findAll(questionRepo.makePredicate(null, null, 2L), pageable);
		log.info("PAGE : " + result.getPageable());

		log.info("----------------");
		result.getContent().forEach(question -> log.info("" + question));
	}

	@Test
	public void testList2() {

		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "qno");
		Page<Question> result = questionRepo.findAll(questionRepo.makePredicate("division", "11", 1L), pageable);
		log.info("PAGE : " + result.getPageable());

		log.info("----------------");
		result.getContent().forEach(question -> log.info("" + question.getQno()));

	}
}