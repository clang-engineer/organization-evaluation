package com.evaluation.controller;

import java.util.HashMap;
import java.util.Map;

import com.evaluation.domain.Staff;
import com.evaluation.service.StaffService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * SurveyConrollerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class SurveyConrollerTests {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private StaffService staffService;

    private MockMvc mockMvc;

    private MockHttpSession session;

    public MockHttpServletRequest request;

    private Staff staff;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        this.staff = staffService.read(207L).orElse(null);

        session = new MockHttpSession();
        session.setAttribute("evaluator", staff);

        request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testMain() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.get("/survey/main").session(session).param("company", "test").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testList() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.get("/survey/list").session(session).param("company", "test").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testEvaluateGet() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/survey/evaluate").session(session)
                .param("company", "test").param("tno", "1")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testEvaluatepost() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/survey/evaluate").param("company", "test")
                .param("tno", "1").param("rno", "10")).andReturn().getModelAndView().getModelMap());
    }

    @Test
    public void testSubmit() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("q1", "1");
        map.put("q2", "2");
        map.put("q3", "3");
        map.put("c1", "test1");
        map.put("c2", "test2");
        map.put("c3", "test3");
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/survey/submit").param("rno", "10")
                .param("finish", "T").param("answer", "" + map)).andReturn().getModelAndView().getModelMap());
    }
}