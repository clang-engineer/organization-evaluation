package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Reply;
import com.evaluation.persistence.ReplyRepository;
import com.evaluation.service.ReplyService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    ReplyRepository replyRepo;

    @Override
    public void register(Reply reply) {
        log.info("register " + reply);

        replyRepo.save(reply);
    }

    @Override
    public Optional<Reply> read(long rno) {
        log.info("read " + rno);

        return replyRepo.findById(rno);
    }

    @Override
    public void modify(Reply reply) {
        log.info("modify " + reply);

        replyRepo.findById(reply.getRno()).ifPresent(origin -> {
            origin.setComment(reply.getComment());
            origin.setUpdateId(reply.getUpdateId());

            replyRepo.save(origin);
        });
    }

    @Override
    public void remove(long rno) {
        log.info("remove " + rno);

        replyRepo.deleteById(rno);
    }

    @Override
    public Optional<List<Reply>> listByMno(long mno) {
        log.info("listby " + mno);

        return replyRepo.listByMno(mno);
    }

}