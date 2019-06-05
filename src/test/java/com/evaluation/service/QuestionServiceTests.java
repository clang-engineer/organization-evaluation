package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.evaluation.domain.Question;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QuestionServiceTests {
	@Setter(onMethod_ = { @Autowired })
	QuestionService questionService;

	@Test
	public void exitsTest() {
		assertNotNull(questionService);
	}

	@Test
	public void registerTest() {
		log.info("register test...");

		Long[] arr = { 1L, 2L, 3L };

		Arrays.stream(arr).forEach(num -> {
			IntStream.range(1, 11).forEach(i -> {
				Question question = new Question();
				question.setDivision1("division1" + i);
				question.setDivision2("division2" + i);
				question.setIdx(i);
				question.setCategory("category" + i);
				question.setItem("question" + i);
				question.setTno(num);
				questionService.register(question);
			});
		});
	}

	@Test
	public void readTest() {
		log.info("read test...");

		log.info("" + questionService.read(1L));
	}

	@Test
	public void modifyTest() {
		log.info("modify test...");
		questionService.read(111L).ifPresent(orgin -> {
			orgin.setCategory("SERVICE MODIFY TEST");
			questionService.modify(orgin);
		});
	}

	@Test
	public void removeTest() {
		log.info("remove test...");

		questionService.remove(112L);
	}

}
