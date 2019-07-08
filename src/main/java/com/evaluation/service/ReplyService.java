package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Reply;

public interface ReplyService {
    void register(Reply reply);

    Optional<Reply> read(long rno);

    void modify(Reply reply);

    void remove(long rno);

    Optional<List<Reply>> listByMno(long mno);

}