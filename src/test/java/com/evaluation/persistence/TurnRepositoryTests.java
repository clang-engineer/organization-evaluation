package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;

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
	TurnRepository repo;

	@Test
	public void testDI() {
		assertNotNull(repo);
	}

	@Test
	public void testGetTurnsOfCompany() {
		repo.getTurnsOfCompany(1L);
	}

	@Test
	public void testGetTurnsInSurvey() {
		repo.getTurnsInSurvey(1L, LocalDateTime.now()).ifPresent(origin -> {
			origin.forEach(turn -> {
				log.info("===>" + turn.getTitle());
			});
		});
	}

	@Test
	public void testGetTurnsInMbo() {
		repo.getTurnsInMbo(1L, LocalDateTime.now()).ifPresent(origin -> {
			origin.forEach(turn -> {
				log.info("===>" + turn.getTitle());
			});
		});
	}
}
