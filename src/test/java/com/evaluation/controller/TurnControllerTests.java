package com.evaluation.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.evaluation.domain.Turn;
import com.google.gson.Gson;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
@Commit
public class TurnControllerTests {

	@Setter(onMethod_ = { @Autowired })
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void testRegister() throws Exception {
		log.info("register...");

		Turn turn = new Turn();
		turn.setTitle("test controller turn 1");
		turn.setType("test controller ");

		String jsonStr = new Gson().toJson(turn);

		log.info(
				"" + mockMvc.perform(MockMvcRequestBuilders.post("/turns/101").contentType(MediaType.APPLICATION_JSON).content(jsonStr)).andReturn());
	}

	@Test
	public void testModify() throws Exception {
		log.info("modify...");

		Turn turn = new Turn();
		turn.setTno(48L);
		turn.setTitle("test Update turn 1");
		turn.setType("test Update controller ");

		String jsonStr = new Gson().toJson(turn);

		log.info("" + mockMvc.perform(MockMvcRequestBuilders.put("/turns/101").contentType(MediaType.APPLICATION_JSON).content(jsonStr)).andReturn());
	}

	@Test
	public void testRemove() throws Exception {
		log.info("remove...");

		log.info("" + mockMvc.perform(MockMvcRequestBuilders.delete("/turns/101/53")).andReturn());
	}

	@Test
	public void testGetList() throws Exception {
		log.info("get List...");

		log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/turns/101")).andReturn());
	}
}
