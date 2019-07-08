package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import com.evaluation.domain.Department;
import com.evaluation.domain.embeddable.Leader;

import org.junit.Before;
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
public class DepartmentRepositoryTests {

    @Autowired
    DepartmentRepository repo;

    @Test
    public void testtDI() {
        assertNotNull(repo);
    }

    @Before
    public void testInsert() {
        IntStream.range(1, 11).forEach(i -> {
            Department department = new Department();
            department.setDepartment1("dep1" + i);
            department.setDepartment2("dep2" + i);
            department.setTno(1L);
            Leader leader = new Leader();
            leader.setSno(1L);
            department.setLeader(leader);
            repo.save(department);
        });

    }

    @Test
    public void testReadList() {

        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "dno");
        Page<Department> result = repo.findAll(repo.makePredicate(null, null, 1L), pageable);
        log.info("PAGE : " + result.getPageable());

        result.getContent().forEach(company -> log.info("" + company));
    }

    @Test
    public void testFindByTnoSno() {
        repo.findByTnoSno(1L, 1L).ifPresent(list -> list.forEach(dep -> {
            log.info("" + dep.getDno());
        }));
    }

    @Test
    public void testGetListDepartment1() {

        repo.getListDepartment1(1L).forEach(origin -> {
            log.info("" + origin);
        });
    }

    @Test
    public void testGetListDepartment2() {
        repo.getListDepartment2(1L).forEach(origin -> {
            log.info("" + origin);
        });
    }

    @Test
    public void deleteByTno() {
        repo.deleteByTno(1L);
    }

    @Test
    public void testFindByDepName() {
        repo.findByDeparment(1L, "dep11", "dep21").ifPresent(origin -> {
            log.info("" + origin.getDno());
        });
    }

    @Test
    public void testUpdate() {
        repo.findByTnoSno(1L, 1L).ifPresent(list -> list.forEach(dep -> {
            repo.findById(dep.getDno()).ifPresent(origin -> {
                origin.setDepartment1("test...");
                repo.save(origin);
            });
        }));
    }

    @Test
    public void testDelete() {
        repo.findByTnoSno(1L, 1L).ifPresent(list -> list.forEach(dep -> {
            repo.deleteById(dep.getDno());
        }));
    }
}
