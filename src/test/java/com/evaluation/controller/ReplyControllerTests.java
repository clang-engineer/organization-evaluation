package com.evaluation.controller;

import com.evaluation.domain.Reply;
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
 * ReplyControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class ReplyControllerTests {

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

        Reply reply = new Reply();
        reply.setMno(1L);
        reply.setComment("test");

        String jsonStr = new Gson().toJson(reply);

        log.info("" + mockMvc
                .perform(MockMvcRequestBuilders.post("/reply").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/reply/1")).andReturn());
    }

    @Test
    public void testModify() throws Exception {
        log.info("register...");

        Reply reply = new Reply();
        reply.setRno(1L);
        reply.setComment("test modify");

        String jsonStr = new Gson().toJson(reply);

        log.info("" + mockMvc
                .perform(MockMvcRequestBuilders.put("/reply").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    
    @Test
    public void testRemove() throws Exception {
        log.info("remove...");

        log.info("" + mockMvc.perform(MockMvcRequestBuilders.delete("/reply/1")).andReturn());
    }
}