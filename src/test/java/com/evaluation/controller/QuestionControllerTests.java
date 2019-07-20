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
 * QuestionControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class QuestionControllerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testRegisterGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/question/register").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRegisterPost() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/question/register").param("tno", "1")
                .param("category", "test").param("item", "test")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/question/view").param("tno", "1").param("qno", "1"))
                .andReturn().getModelAndView().getModelMap());
    }

    @Test
    public void testModfiyGet() throws Exception {
        log.info(
                "" + mockMvc.perform(MockMvcRequestBuilders.get("/question/modify").param("tno", "1").param("qno", "1"))
                        .andReturn().getModelAndView().getModelMap());
    }

    @Test
    public void testModifyPost() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/question/modify").param("tno", "1").param("qno", "1")
                        .param("category", "test modify").param("item", "test modify"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRemove() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/question/remove").param("tno", "1").param("qno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testGetList() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/question/list").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }

    @Test
    public void testXlDownload() throws Exception {
        log.info(""
                + mockMvc.perform(MockMvcRequestBuilders.post("/question/xlDownload").param("tno", "1")).andReturn());
    }
}