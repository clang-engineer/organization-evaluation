package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Reply;

public interface ReplyService {
    public void register(Reply reply);

    public Optional<Reply> read(long rno);

    public void modify(Reply reply);

    public void remove(long rno);

    public Optional<List<Reply>> listByMno(long mno);

}