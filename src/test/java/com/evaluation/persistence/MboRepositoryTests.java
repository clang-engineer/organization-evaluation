package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import com.evaluation.domain.Mbo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class MboRepositoryTests {

    @Autowired
    MboRepository repo;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Before
    public void insertTest() {
        IntStream.range(1, 11).forEach(i -> {
            Mbo mbo = new Mbo();
            mbo.setObject("목표" + i);
            mbo.setProcess("12월까지 땡땡을 한다." + i);
            mbo.setRatio(0.1 + i);
            mbo.setFinish("Y");
            mbo.setTno(1L);
            mbo.setSno(1L);

            repo.save(mbo);
        });
    }

    @Test
    public void testListByTnoSno() {
        repo.listByTnoSno(1L, 1L).ifPresent(origin -> {
            origin.forEach(list -> {
                log.info("" + list);
            });
        });
    }

    @Test
    public void testListByTno() {
        repo.listByTno(1L).ifPresent(origin -> {
            origin.forEach(list -> {
                log.info("" + list);
            });
        });
    }

    @Test
    public void testRatioByTnoSno() {
        repo.ratioByTnoSno(1L, 1L).ifPresent(list -> {
            list.forEach(ratio -> {
                log.info("" + ratio);
            });
        });
    }

    @Test
    public void testRecentChangeInObject() {
        log.info("" + repo.recentChangeOfEvaluatedList(1L, 207L, 0));
    }
}