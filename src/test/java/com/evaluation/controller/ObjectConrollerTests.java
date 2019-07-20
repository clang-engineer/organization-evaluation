package com.evaluation.controller;

import com.evaluation.domain.Mbo;
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
 * ObjectConrollerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class ObjectConrollerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testRegister() throws Exception {
        log.info("register...");

        Mbo mbo = new Mbo();
        mbo.setObject("test");
        String jsonStr = new Gson().toJson(mbo);

        log.info("" + mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/object").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/objecgt/1")).andReturn());
    }

    @Test
    public void testModify() throws Exception {

        Mbo mbo = new Mbo();
        mbo.setObject("test modify");
        String jsonStr = new Gson().toJson(mbo);

        log.info("" + mockMvc.perform(
                MockMvcRequestBuilders.put("/object/1/do").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    @Test
    public void testRemove() throws Exception {
        log.info("remove...");

        log.info("" + mockMvc.perform(MockMvcRequestBuilders.delete("/object/1/do")).andReturn());
    }

    @Test
    public void testTeamObjectRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/object/department/1")).andReturn());
    }

    @Test
    public void testTeamObjectModify() throws Exception {
        Leader leader = new Leader();
        leader.setTitle("test");
        String jsonStr = new Gson().toJson(leader);

        log.info("" + mockMvc.perform(MockMvcRequestBuilders.post("/object/department/1")
                .contentType(MediaType.APPLICATION_JSON).content(jsonStr)).andReturn());
    }
}