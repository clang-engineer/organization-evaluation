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
 * HomeControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class HomeControllerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testHome() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/")).andReturn().getModelAndView().getViewName());
    }

    @Test
    public void testAdminLogin() throws Exception {
        log.info(
                "" + mockMvc.perform(MockMvcRequestBuilders.get("/login")).andReturn().getModelAndView().getViewName());
    }

    @Test
    public void testAccessDenied() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/accessDenied")).andReturn().getModelAndView()
                .getViewName());
    }

    @Test
    public void testLogOut() throws Exception {
        log.info(""
                + mockMvc.perform(MockMvcRequestBuilders.get("/logout")).andReturn().getModelAndView().getViewName());
    }

    @Test
    public void testUserLoginrGet() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.get("/mbo").servletPath("/mbo").param("company", "test")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserLoginrPost() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/mbo/login").param("company", "test").param("tno", "1")
                        .param("email", "test@test.com").param("password", "1234"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserLogOut() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/mbo/logout").servletPath("/mbo/logout").param("company", "test"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserProfileGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/mbo/profile").servletPath("/mbo/profile")
                .param("company", "test").param("tno", "1")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserProfileModifyGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/mbo/modify").servletPath("/mbo/modify")
                .param("company", "test").param("tno", "1")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserProfileModifyPost() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/mbo/modify").servletPath("/mbo/modify")
                .param("company", "test").param("tno", "1").param("email", "test@test.com")).andReturn()
                .getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserContactGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/mbo/contact").servletPath("/mbo/contact")
                .param("company", "test").param("tno", "1")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testUserContactPost() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/mbo/help").servletPath("/mbo/help")
                .param("tno", "1").param("content", "test")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

}