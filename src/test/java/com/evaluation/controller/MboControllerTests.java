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
 * MboControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class MboControllerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/register").param("uid", "testid").param("uname", "testname")
                .param("upw", "test").param("roles", "ADMIN"));
    }

    @Test
    public void testLoginrGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/mbo/").param("company", "test")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testLoginrPost() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/mbo/login").param("company", "test").param("tno", "1")
                        .param("email", "test@test.com").param("password", "1234"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testLogOut() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/mbo/logout").param("company", "test"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testContact() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.get("/mbo/contact").param("company", "test").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }
}