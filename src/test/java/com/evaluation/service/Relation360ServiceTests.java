package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Relation360ServiceTests {

    @Setter(onMethod_ = { @Autowired })
    Relation360Service relation360Service;

    @Setter(onMethod_ = { @Autowired })
    StaffService staffService;

    @Test
    public void diTest() {
        assertNotNull(relation360Service);
    }

    @Test
    public void registerTest() {
        Staff evaluated = staffService.read(2L).get();
        Staff evaluator = staffService.read(15L).get();

        Relation360 relation360 = new Relation360();
        relation360.setEvaluated(evaluated);
        relation360.setEvaluator(evaluator);
        relation360.setRelation("me");
        relation360.setFinish("N");
        relation360.setTno(9L);

        relation360Service.register(relation360);
    }

    @Test
    public void readTest() {
        log.info("read ---->" + relation360Service.read(60L).get().getEvaluator());
    }

    @Test
    public void removeTest() {
        relation360Service.remove(61L);
    }

    @Test
    public void getDistinctEvaluatedListTest() {
        PageVO vo = new PageVO();
        vo.setType("evaluator");
        vo.setKeyword("id2");

        Page<Staff> result = relation360Service.getDistinctEvaluatedList(9L, vo);

        result.getContent().forEach(staff -> log.info("" + staff.getSno()));
    }

    // @Test
    // public void findByTnoTests() {
    //     List<Relation360> result = relation360Service.getAllList(10L);
    //     result.forEach(origin -> log.info("" + origin.getRno()));
    // }

}