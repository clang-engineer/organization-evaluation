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
 * ContentControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class CommentControllerTests {
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testRegisterPost() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/comment/register").param("tno", "1").param("comment", "test"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testModify() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/comment/modify").param("tno", "1")
                .param("idx", "1").param("comment", "testmoidfy")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRemove() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/comment/remove").param("tno", "1").param("idx", "1")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testList() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/comment/list").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }
}