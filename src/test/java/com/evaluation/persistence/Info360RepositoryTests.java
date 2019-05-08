package com.evaluation.persistence;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Info360;
import com.evaluation.domain.Turn;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Commit
public class Info360RepositoryTests {

	@Autowired
	Info360Repository info360repo;

	@Test
	public void turnTests() {
		log.info("============");
		log.info("" + info360repo);
	}

	@Test
	public void testInsertTurns() {

		Long[] arr = { 1L, 2L, 3L };

		Arrays.stream(arr).forEach(num -> {

			Turn turn = new Turn();
			turn.setTno(num);

			Info360 info360 = new Info360();
			info360.setTno(num);
			info360.setTitle("test " + num);
			info360.setContent("content " + num);
			info360.setTurn(turn);
			info360repo.save(info360);
		});

	}
}
