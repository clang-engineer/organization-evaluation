package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import com.evaluation.domain.Division;

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
public class DivisionRepositoryTests {

    @Autowired
    DivisionRepository repo;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Before
    public void setBefore() {
        IntStream.range(1, 11).forEach(i -> {
            Division division = new Division();
            division.setDivision1("div1" + i);
            division.setDivision2("div2" + i);
            division.setCno(1L);
            repo.save(division);
        });
    }

    @Test
    public void testDeleteByCno() {
        repo.deleteByCno(1L);
    }

    @Test
    public void testGetListDivision1() {
        repo.getListDivision1(1L).forEach(origin -> {
            log.info("" + origin);
        });
    }

    @Test
    public void testGetListDivision2() {
        repo.getListDivision2(1L).forEach(origin -> {
            log.info("" + origin);
        });
    }
}
