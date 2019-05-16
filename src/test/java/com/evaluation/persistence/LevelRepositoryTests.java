package com.evaluation.persistence;

import java.util.stream.IntStream;

import com.evaluation.domain.Department;
import com.evaluation.domain.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
@Slf4j
public class LevelRepositoryTests {

    @Autowired
    LevelRepository levelRepo;

    @Test
    public void insertLevelDummies() {

        IntStream.range(1, 11).forEach(i -> {

            Level level = new Level();
            level.setContent("lev1" + i);
            level.setCno(99L);
            levelRepo.save(level);
        });

    }

    // @Test
    // public void testList1() {
    // Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "cno");
    // Page<Company> result = repo.findAll(repo.makePredicate(null, null),
    // pageable);
    // log.info("PAGE : " + result.getPageable());

    // log.info("----------------");
    // result.getContent().forEach(company -> log.info("" + company));
    // }

    @Test
    public void testList2() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "lno");
        Page<Level> result = levelRepo.findAll(levelRepo.makePredicate("level", "1", 99L), pageable);
        log.info("PAGE : " + result.getPageable());

        log.info("----------------");
        result.getContent().forEach(level -> log.info("" + level));
    }

}
