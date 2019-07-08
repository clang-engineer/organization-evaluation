package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;

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
public class RelationMboTests {

    @Autowired
    RelationMboRepository repo;

    @Autowired
    StaffRepository staffRepo;

    public static Staff staff;
    public static RelationMbo relationMbo;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Before
    public void setBefore() {

        staff = new Staff();
        relationMbo = new RelationMbo();
        staff.setEmail("test@test.com");
        staffRepo.save(staff);

        relationMbo.setTno(1L);
        relationMbo.setEvaluated(staff);
        repo.save(relationMbo);

        IntStream.range(1, 31).forEach(i -> {
            RelationMbo relationMbo = new RelationMbo();
            relationMbo.setTno(1L);
            repo.save(relationMbo);
        });
    }

    @Test
    public void findByEvaulated() {
        repo.findByEvaulated(1L, staff.getSno()).ifPresent(origin -> {
            log.info("" + origin);
        });
    };

    @Test
    public void testGetDistinctEvaluatedList() {
        repo.getDistinctEvaluatedList(1L, null);
        repo.getDistinctEvaluatedListByEvaluated(1L, null, null);
        repo.getDistinctEvaluatedListByEvaluator(1L, null, null);
    };

    @Test
    public void testFindByEvaluatorEmail() {
        repo.findByEvaluatorEmail(1L, staff.getEmail()).ifPresent(origin -> {
            log.info("" + origin);
        });
    };

    @Test
    public void testFindByEvaluator() {
        repo.findByEvaluatorEmail(1L, staff.getEmail()).ifPresent(origin -> {
            log.info("" + origin);
        });
    };

    @Test
    public void testFindAllByTno() {
        repo.findAllByTno(1L).ifPresent(origin -> {
            log.info("" + origin);
        });
    };

    @Test
    public void testFindDintinctEavluatedByTno() {
        repo.findDintinctEavluatedByTno(1L).forEach(origin -> {
            log.info("" + origin);
        });
    };

    @Test
    public void testProgressOfSurevey() {
        repo.progressOfSurevey(1L).ifPresent(origin -> {
            origin.forEach(data -> log.info("" + data));
        });
    };

    @Test
    public void testFindMeRelationByTnoSno() {
        repo.findMeRelationByTnoSno(1L, staff.getSno()).ifPresent(origin -> {
            log.info("" + origin);
        });
    };

    @Test
    public void testProgressOfPlan()  {
        repo.progressOfPlan(1L).ifPresent(origin -> {
            log.info("" + origin);
        });
    };
}