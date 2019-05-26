package com.evaluation.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

			Company company = new Company();
			company.setCno(num);

			IntStream.range(1, 11).forEach(i -> {
				Turn turn = new Turn();
				turn.setTitle("turn..." + i);
				List<String> types = new ArrayList<>();
				types.add("360");
				types.add("mbo");
				turn.setTypes(types);
				turn.setCompany(company);
				turnRepo.save(turn);
			});
		});

	}
}
