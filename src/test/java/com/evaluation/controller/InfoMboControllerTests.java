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

import lombok.extern.slf4j.Slf4j;

/**
 * InfoMboControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class InfoMboControllerTests {
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/infoMbo/read").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }

    @Test
    public void testModfiyGet() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/infoMbo/read").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }

    @Test
    public void testModifyPost() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/infoMbo/modify").param("tno", "1")
                .param("title", "testmoidfy").param("content", "test")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }
}