package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import com.evaluation.domain.HelpDesk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class HelpDeskRepositoryTests {

    @Autowired
    HelpDeskRepository repo;

    @Test
    public void testtDI() {
        assertNotNull(repo);
    }

    @Test
    public void testRead() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "hno");
        Page<HelpDesk> result = repo.findAll(repo.makePredicate(null, null), pageable);
        log.info("PAGE : " + result.getPageable());

        result.getContent().forEach(origin -> log.info("" + origin));
    }

}