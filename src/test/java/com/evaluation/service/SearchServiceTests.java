package com.evaluation.service;

import java.util.List;
import java.util.Map;

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
    private DistinctInfoService searchService;

    @Test
    public void test() {
        Map<String, Object> result = searchService.getDistinctInfo(99L);
        for (String key : result.keySet()) {
            log.info("" + key + result.get(key));
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