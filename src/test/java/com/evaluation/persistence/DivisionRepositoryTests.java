package com.evaluation.persistence;

import java.util.stream.IntStream;

import com.evaluation.domain.Division;

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
@Slf4j
@Commit
public class DivisionRepositoryTests {

    @Autowired
    DivisionRepository divisionRepo;

    @Test
    public void insertDivisionDummies() {

        IntStream.range(1, 11).forEach(i -> {

            Division division = new Division();
            division.setDivision1("dev1" + i);
            division.setDivision2("dev2" + i);
            division.setCno(99L);
            divisionRepo.save(division);
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
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "cno");
        Page<Division> result = divisionRepo.findAll(divisionRepo.makePredicate("division", "11", 99L), pageable);
        log.info("PAGE : " + result.getPageable());

        log.info("----------------");
        result.getContent().forEach(division -> log.info("" + division));
    }

}
