package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import com.evaluation.domain.Mbo;

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
public class MboRepositoryTests {

    @Setter(onMethod_ = { @Autowired })
    MboRepository mboRepo;

    @Test
    public void testDI() {
        assertNotNull(mboRepo);
    }

    @Test
    public void insertTest() {

        Mbo mbo = new Mbo();
        mbo.setObject("목표 1");
        mbo.setProcess("12월까지 땡땡을 한다.");
        mbo.setRatio(0.1);
        mbo.setFinish("Y");
        mbo.setTno(1L);
        mbo.setSno(1L);

        mboRepo.save(mbo);
    }

    @Test
    public void listTest() {
        mboRepo.listByTnoSno(1L, 1L).ifPresent(origin -> {
            origin.forEach(mbo -> {
                log.info("" + mbo.getMno());
            });
        });

    }

    @Test
    public void getRatioTest() {
        mboRepo.ratioByTnoSno(4L, 1114L).ifPresent(list -> {
            list.forEach(ratio -> {
                log.info("" + ratio);
            });
        });
    }

    @Test
    public void name() {
        mboRepo.listByTno(1L).ifPresent(origin -> {
            log.info("" + origin);
        });
    }
}