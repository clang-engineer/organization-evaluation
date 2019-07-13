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
 * BookControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class BookControllerTests {
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testRegisterGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/book/register")).andReturn().getModelAndView()
                .getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRegisterPost() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.post("/book/register").param("type", "360Reply").param("title", "testTitle1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/book/read").param("bno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }

    @Test
    public void testModify() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/book/modify").param("bno", "1").param("title", "testmoidfy"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRemove() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/book/remove").param("bno", "1")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testList() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/book/list")).andReturn().getModelAndView()
                .getModelMap());
    }

    @Test
    public void testGetContents() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/book/contents").param("bno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }
}