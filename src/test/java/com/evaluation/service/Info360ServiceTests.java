package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Info360;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Info360ServiceTests {

	@Setter(onMethod_ = { @Autowired })
	Info360Service service;

	@Test
	public void testExist() {
		log.info("========== test exist service");
		assertNotNull(service);
	}

	@Test
	public void testRegister() {
		log.info("========== test register");

		Info360 info360 = new Info360();

		info360.setTno(2L);
		info360.setTitle("test...");

		service.register(info360);
	}

	@Test
	public void testGet() {
		log.info("========== test get");
		log.info("" + service.get(1L));
	}

	@Test
	public void testModify() {
		log.info("========== test Modify");
		Info360 info360 = new Info360();

		info360.setTno(1L);
		info360.setTitle("test title modify 1");

		service.register(info360);

	}

	@Test
	public void testDelete() {
		log.info("========== test delete");
		service.remove(1L);
	}

}
