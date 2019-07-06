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
		turnRepo.deleteAll();
		Long[] arr = { 1L, 2L, 3L, 4L, 5L };

		Arrays.stream(arr).forEach(num -> {

			IntStream.range(1, 4).forEach(i -> {
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
		turnRepo.getTurnsInSurvey(1L, LocalDateTime.now()).ifPresent(origin -> {
			origin.forEach(turn -> {
				log.info("===>" + turn.getTitle());
			});
		});
	}

	@Test
	public void testGetTurnsOfCompanyByStatusMBO() {
		turnRepo.getTurnsInMBO(1L, LocalDateTime.now()).ifPresent(origin -> {
			origin.forEach(turn -> {
				log.info("===>" + turn.getTitle());
			});
		});
	}
}
