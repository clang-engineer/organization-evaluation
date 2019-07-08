package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import com.evaluation.domain.Reply;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class ReplyRepositoryTests {

    @Setter(onMethod_ = { @Autowired })
    ReplyRepository repo;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Before
    public void insertTest() {
        Reply reply = new Reply();
        reply.setComment("댓글 test");
        reply.setMno(1L);

        repo.save(reply);
    }

    @Test
    public void testListByMno() {
        log.info("====>" + repo.listByMno(1L));
    }
}