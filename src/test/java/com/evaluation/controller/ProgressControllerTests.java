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
 * ProgressControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class ProgressControllerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testProgressList() throws Exception {
        String resultPage = mockMvc.perform(
                MockMvcRequestBuilders.get("/progress/survey").param("tno", "1").servletPath("/progress/survey"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testEvaluatedList() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.get("/progress/survey/evaluatedList")
                .param("tno", "1").param("sno", "1").servletPath("/progress/survey")).andReturn().getModelAndView()
                .getViewName();
        log.info(resultPage);
    }

    @Test
    public void testEvaluatedFinishChange() throws Exception {

        log.info("" + mockMvc.perform(MockMvcRequestBuilders.put("/progress/survey/evaluatedList").param("rno", "1")
                .param("finish", "T").servletPath("/progress/survey/evaluatedList")).andReturn());
    }

    @Test
    public void testSurveyXlDownload() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/progress/survey").param("tno", "1")).andReturn());
    }

    @Test
    public void testSurveyResultDownload() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/progress/survey/result").param("tno", "1"))
                .andReturn());
    }

    @Test
    public void testMboXlDownload() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/progress/mbo").param("tno", "1")).andReturn());
    }

    @Test
    public void testMboResultDownload() throws Exception {
        log.info(""
                + mockMvc.perform(MockMvcRequestBuilders.post("/progress/mbo/result").param("tno", "1")).andReturn());
    }

    @Test
    public void testProgressOfPlan() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/progress/mbo/plan").param("tno", "1")).andReturn());
    }

    @Test
    public void testObjectList() throws Exception {
        log.info("" + mockMvc
                .perform(MockMvcRequestBuilders.get("/progress/mbo/objectList").param("tno", "1").param("sno", "1"))
                .andReturn());
    }

    @Test
    public void testPlanXlDownload() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/progress/mbo/plan").param("tno", "1")).andReturn());
    }

    @Test
    public void testPlanResultDownload() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/progress/mbo/plan/result").param("tno", "1")).andReturn());
    }
}