package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import com.evaluation.domain.Turn;

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

		Turn turn = new Turn();
		turn.setTitle("test title 1");
		Set<String> types = new HashSet<String>();
		types.add("360");
		types.add("mbo");
		turn.setTypes(types);
		turn.setCno(100L);
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
		Set<String> types = new HashSet<>();
		types.add("360");
		types.add("mbo");
		turn.setTypes(types);

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

		service.getList(100L).ifPresent(origin -> {
			origin.forEach(turn -> log.info("" + turn));
		});
	}

}
