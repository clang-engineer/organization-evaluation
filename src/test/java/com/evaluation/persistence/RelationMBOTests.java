package com.evaluation.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
@Slf4j
public class RelationMboTests {

    @Setter(onMethod_ = { @Autowired })
    RelationMBORepository relationMBORepo;

    @Test
    public void name() {
        relationMBORepo.findMeRelationByTnoSno(1, 2).ifPresent(origin -> {
            log.info("" + origin.getRno());
        });
    }

    @Test
    public void progressOfPlan() {
        relationMBORepo.progressOfPlan(1).ifPresent(origin -> {
            log.info("" + origin);
        });
    }
}