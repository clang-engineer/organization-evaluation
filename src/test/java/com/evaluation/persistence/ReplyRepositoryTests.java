package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import com.evaluation.domain.Reply;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
public class ReplyRepositoryTests {

    @Setter(onMethod_ = { @Autowired })
    ReplyRepository replyRepo;

    @Test
    public void testDI() {
        assertNotNull(replyRepo);
    }

    @Test
    public void insertTest() {
        Reply reply = new Reply();
        reply.setComment("댓글 test");
        reply.setMno(1L);

        replyRepo.save(reply);
    }
}