package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.embeddable.InfoSurvey;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Info360ServiceTests {

	@Setter(onMethod_ = { @Autowired })
	InfoSurveyService info360Service;

	@Setter(onMethod_ = { @Autowired })
	TurnService turnService;

	@Test
	public void testExist() {
		log.info("========== test exist service");
		assertNotNull(info360Service);
	}

	@Test
	public void testRegister() {
		log.info("========== test register");

		InfoSurvey info360 = new InfoSurvey();
		info360.setTitle("제1회..");
		info360Service.register(1L, info360);
	}

	@Test
	public void testGet() {
		log.info("========== test get");
		log.info("" + info360Service.read(1L));
	}

	@Test
	public void testModify() {
		log.info("========== test Modify");
		InfoSurvey info360 = new InfoSurvey();

		info360.setTitle("test title modify 1");

		info360Service.register(1L, info360);

	}

	@Test
	public void testDelete() {
		log.info("========== test delete");
		info360Service.remove(1L);
	}

}
