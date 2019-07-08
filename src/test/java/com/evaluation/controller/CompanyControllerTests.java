package com.evaluation.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class CompanyControllerTests {

	@Setter(onMethod_ = { @Autowired })
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		mockMvc.perform(MockMvcRequestBuilders.post("/company/register").param("id", "mockTest1").param("name",
				"mock test name1").param("cno","1"));
	}

	@Test
	public void testList() throws Exception {
		log.info("=>" + mockMvc.perform(MockMvcRequestBuilders.get("/company/list")).andReturn().getModelAndView()
				.getModelMap());
	}

	@Test
	public void testRegister() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/company/register")
				.param("id", "mock test id1").param("name", "mock test name1")).andReturn().getModelAndView()
				.getViewName();
		log.info(resultPage);
	}

	@Test
	public void testView() throws Exception {
		log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/company/view").param("cno", "1")).andReturn()
				.getModelAndView().getModelMap());
	}

	@Test
	public void testModify() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/company/modify").param("cno", "1")
				.param("id", "mock modify id").param("name", "mock modify name")).andReturn().getModelAndView()
				.getViewName();

		log.info(resultPage);
	}

	@Test
	public void testRemove() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/company/delete").param("cno", "1"))
				.andReturn().getModelAndView().getViewName();

		log.info(resultPage);

	}
}
