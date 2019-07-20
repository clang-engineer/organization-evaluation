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
 * StaffControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class StaffControllerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testRegisterGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/staff/register").param("tno", "1")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRegisterPost() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.post("/staff/register").param("tno", "1").param("email", "test@test.com"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/staff/view").param("tno", "1").param("sno", "1"))
                .andReturn().getModelAndView().getModelMap());
    }

    @Test
    public void testModifyGet() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/staff/modify").param("tno", "1").param("sno", "1")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testModifyPost() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.post("/staff/modify").param("tno", "1").param("sno", "1").param("name", "test"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRemove() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/staff/remove").param("tno", "1").param("sno", "1")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testReadList() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/staff/list").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }

    @Test
    public void testXlDownload() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/staff/xlDownload").param("tno", "1")).andReturn());
    }
}