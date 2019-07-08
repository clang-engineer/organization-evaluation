package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import com.evaluation.domain.Level;

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
public class LevelRepositoryTests {

    @Autowired
    LevelRepository repo;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Test
    public void setBefore() {
        IntStream.range(1, 11).forEach(i -> {
            Level level = new Level();
            level.setContent("lev" + i);
            level.setCno(1L);
            repo.save(level);
        });
    }

    @Test
    public void testDeleteByCno() {
        repo.deleteByCno(1L);
    }

    @Test
    public void testGetListLevel() {
        repo.getListLevel(1L);
    }

    @Test
    public void testList2() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "lno");
        Page<Level> result = repo.findAll(repo.makePredicate("level", "1", 99L), pageable);
        log.info("PAGE : " + result.getPageable());

        result.getContent().forEach(level -> log.info("" + level));
    }

}
