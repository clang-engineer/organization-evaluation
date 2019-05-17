package com.evaluation.persistence;

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
public class SearchRepositoryTests {
    @Setter(onMethod_ = { @Autowired })
    SearchRepository searchRepo;

    @Test
    public void testAutowired() {
        log.info("===>" + searchRepo);
    }

    @Test
    public void testDepartment1() {
        List<String> result = searchRepo.getListDepartment1(98L);
        log.info("" + result);
    }
}