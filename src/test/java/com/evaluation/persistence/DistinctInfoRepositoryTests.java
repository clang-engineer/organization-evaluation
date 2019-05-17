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
public class DistinctInfoRepositoryTests {
    @Setter(onMethod_ = { @Autowired })
    DistinctInfoRepository repo;

    @Test
    public void testAutowired() {
        log.info("===>" + repo);
    }

    @Test
    public void testDepartment1() {
        List<String> result = repo.getListDepartment1(98L);
        log.info("" + result);
    }

    @Test
    public void testCategory() {
        List<String> result = repo.getListCategory(1L);
        log.info("" + result);

    }
}