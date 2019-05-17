package com.evaluation.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SearchServiceTests {

    @Setter(onMethod_ = { @Autowired })
    private SearchService searchService;

    @Test
    public void test() {
        ArrayList<List<String>> result = searchService.getDistinctInfo(99L);
        for (Object res : result) {
            log.info("" + res);
        }
    }

    @Test
    public void testListLevel() {
        List<String> result = searchService.getListLevel(99L);
        for (Object res : result) {
            log.info("" + res);
        }
    }
}