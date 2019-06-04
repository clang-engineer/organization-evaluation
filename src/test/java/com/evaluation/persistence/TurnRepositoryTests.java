package com.evaluation.persistence;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import com.evaluation.domain.Turn;

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
public class TurnRepositoryTests {
	@Autowired
	TurnRepository turnRepo;

	@Test
	public void turnTests() {
		log.info("===========================");
		log.info("" + turnRepo);
	}

	@Test
	public void testInsertTurns() {

		Long[] arr = { 100L, 99L, 98L };

		Arrays.stream(arr).forEach(num -> {

			IntStream.range(1, 11).forEach(i -> {
				Turn turn = new Turn();
				turn.setTitle("turn..." + i);
				Set<String> types = new HashSet<>();
				types.add("360");
				types.add("mbo");
				turn.setTypes(types);
				turn.setCno(num);
				turnRepo.save(turn);
			});
		});

	}

	@Test
	public void testGetTurnsOfCompanyByStatus() {
		turnRepo.getTurnsInSurvey(1L, LocalDateTime.now()).forEach(origin -> log.info("" + origin.getTitle()));
	}
}
