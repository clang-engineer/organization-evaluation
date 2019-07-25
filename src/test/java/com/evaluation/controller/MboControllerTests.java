package com.evaluation.controller;

import java.util.HashMap;
import java.util.Map;

import com.evaluation.domain.Staff;
import com.evaluation.domain.embeddable.RatioValue;
import com.evaluation.service.StaffService;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @Autowired
    private StaffService staffService;

    private MockMvc mockMvc;

    private MockHttpSession session;

    public MockHttpServletRequest request;

    private Staff staff;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/register").param("uid", "testid").param("uname", "testname")
                .param("upw", "test").param("roles", "ADMIN"));
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
                MockMvcRequestBuilders.get("/mbo/main").session(session).param("company", "test").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void recentChange() throws Exception {
        log.info(
                "" + mockMvc.perform(MockMvcRequestBuilders.get("/mbo/recentChange/1/1").session(session)).andReturn());
    }

    @Test
    public void testList() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.get("/mbo/list").session(session).param("company", "test").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testObjectGet() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.get("/mbo/object").session(session).param("company", "test").param("tno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testObjectpost() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/mbo/object").param("company", "test")
                .param("tno", "1").param("rno", "10")).andReturn().getModelAndView().getModelMap());
    }

    @Test
    public void testNoteCreate() throws Exception {

        String note = "test";
        String jsonStr = new Gson().toJson(note);

        log.info("" + mockMvc.perform(
                MockMvcRequestBuilders.post("/note/1/plan").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    @Test
    public void testNoteRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/note/1/plan")).andReturn());
    }

    @Test
    public void testSubmit() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("rno", "1L");
        map.put("finish", "T");
        RatioValue value = new RatioValue();
        value.setRatio(1.1);
        value.setValue(2.2);
        map.put("key", "" + value);
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.put("/mbo/submit").param("answer", "" + map)).andReturn());
    }
}