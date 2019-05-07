package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TurnServiceTests {

	@Setter(onMethod_ = { @Autowired })
	TurnService service;

	@Test
	public void testExist() {
		log.info("========== test exist service");
		log.info("" + service);
		assertNotNull(service);
	}

	@Test
	public void testRegister() {
		log.info("========== test register");

		Company company = new Company();
		company.setCno(100L);

		Turn turn = new Turn();
		turn.setTitle("test title 1");
		turn.setType("test type 1");
		turn.setCompany(company);
		service.register(turn);

	}

	@Test
	public void testGet() {
		log.info("========== test get");
		log.info("" + service.get(1L));
	}

	@Test
	public void testModify() {
		log.info("========== test Modify");
		Turn turn = new Turn();

		turn.setTno(2L);
		turn.setTitle("test title modify 1");
		turn.setType("test modify 1");

		service.register(turn);

	}

	@Test
	public void testDelete() {
		log.info("========== test delete");
		service.remove(1L);
	}

	@Test
	public void testGetList() {
		log.info("========== test getList");
		Company company = new Company();
		company.setCno(100L);
		service.getLists(company).forEach(turn -> log.info("" + turn));
		;
	}

}
