package com.evaluation.persistence;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Commit
public class TurnTests {
	@Autowired
	TurnRepository turnRepo;

	@Test
	public void turnTests() {
		log.info("===========================");
		log.info("" + turnRepo);
	}

	@Test
	public void testInsertTurns() {

		Long[] arr = { 300L, 299L, 288L };

	Arrays.stream(arr).forEach(num->{
		
		Company company = new Company();
		company.setCno(num);

		IntStream.range(0, 10).forEach(i -> {
			Turn turn = new Turn();
			turn.setTitle("turn..." + i);
			turn.setSurveyType("type..." + i);
			turn.setCompany(company);
			turnRepo.save(turn);
		});
		});

	}
}
