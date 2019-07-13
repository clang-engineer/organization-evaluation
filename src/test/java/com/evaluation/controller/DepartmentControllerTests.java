package com.evaluation.controller;

import com.evaluation.domain.embeddable.Leader;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * DepartmentControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class DepartmentControllerTests {

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
                .perform(MockMvcRequestBuilders.post("/department/register").param("tno", "1")
                        .param("department1", "testdpe1").param("department2", "testdep2"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testModify() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/department/modify").param("tno", "1").param("dno", "1")
                        .param("department1", "modifydpe1").param("department2", "modifydep2"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRemove() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/department/remove").param("tno", "1").param("dno", "1"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testList() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/department/list").param("tno", "1")).andReturn()
                .getModelAndView().getModelMap());
    }

    @Test
    public void testReadLeader() throws Exception {
        log.info(""
                + mockMvc.perform(MockMvcRequestBuilders.get("/department/leader").param("tno", "1").param("dno", "1"))
                        .andReturn().getModelAndView().getModelMap());
    }

    @Test
    public void testRegisterLeaderPost() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/department/leader/register").param("tno", "1").param("dno", "1")
                        .param("title", "testTitle").param("content", "testContent"))
                .andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }

    @Test
    public void testRESTRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/department/2")).andReturn());
    }

    @Test
    public void testRestModify() throws Exception {

        Leader leader = new Leader();
        leader.setSno(2L);
        leader.setTitle("mod title");
        leader.setContent("mod content");

        String jsonStr = new Gson().toJson(leader);
        log.info("" + mockMvc.perform(
                MockMvcRequestBuilders.put("/department/2").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

}