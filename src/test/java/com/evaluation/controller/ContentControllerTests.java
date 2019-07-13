package com.evaluation.controller;

import com.evaluation.domain.embeddable.Content;
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
 * ContentControllerTests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@Slf4j
public class ContentControllerTests {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();

    }

    @Test
    public void testAddContent() throws Exception {
        log.info("register...");

        Content content = new Content();
        content.setName("test name");
        content.setRatio(10);

        String jsonStr = new Gson().toJson(content);

        log.info("" + mockMvc.perform(
                MockMvcRequestBuilders.post("/contents/1").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    @Test
    public void testRead() throws Exception {
        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/contents/1/1")).andReturn());
    }

    @Test
    public void testModify() throws Exception {

        Content content = new Content();
        content.setName("test name");
        content.setRatio(10);

        String jsonStr = new Gson().toJson(content);

        log.info("" + mockMvc.perform(
                MockMvcRequestBuilders.put("/contents/1/1").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                .andReturn());
    }

    @Test
    public void testRemove() throws Exception {
        log.info("remove...");

        log.info("" + mockMvc.perform(MockMvcRequestBuilders.delete("/contents/1/1")).andReturn());
    }

    @Test
    public void testGetLists() throws Exception {
        log.info("get List...");

        log.info("" + mockMvc.perform(MockMvcRequestBuilders.get("/contents/1")).andReturn());
    }

}