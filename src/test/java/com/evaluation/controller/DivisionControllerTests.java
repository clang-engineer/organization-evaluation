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
 * DivisionControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class DivisionControllerTests {
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
                .perform(MockMvcRequestBuilders.post("/division/register").param("tno", "1")
                        .param("division1", "test div1").param("division2", "test div2"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testModify() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/division/modify").param("tno", "1").param("dno", "1")
                        .param("division1", "testmoidfy").param("division2", "testmoidfy2"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRemove() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/division/remove").param("tno", "1").param("dno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testList() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/division/list").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }
}