package com.evaluation.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.evaluation.domain.Relation360;

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
public class Relation360ServiceTests {

    @Setter(onMethod_ = { @Autowired })
    Relation360Service relation360Service;

    @Test
    public void diTest() {
        assertNotNull(relation360Service);
    }

    @Test
    public void registerTest() {
        Relation360 relation360 = new Relation360();
        relation360.setEvaluated("evaluated rel1");
        relation360.setEvaluator("evaluator rel1");
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
}