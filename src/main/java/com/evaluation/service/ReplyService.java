package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Reply;

/**
 * <code>ReplyService</code> 객체는 Reply객체 sevice 계층의 interface를 표현한다.
 */
public interface ReplyService {
    /**
     * 댓글 정보를 등록한다.
     * 
     * @param reply 댓글 정보
     */
    void register(Reply reply);

    /**
     * 댓글 정보를 읽어온다.
     * 
     * @param rno 댓글 id
     * @return 댓글 정보
     */
    Optional<Reply> read(long rno);

    /**
     * 댓글 정보를 수정한다.
     * 
     * @param reply 댓글 정보
     */
    void modify(Reply reply);

    /**
     * 댓글 정보를 삭제한다.
     * 
     * @param rno 댓글 id
     */
    void remove(long rno);

    /**
     * 한 목표의 모든 댓글을 찾는다.
     * 
     * @param mno 목표 id
     * @return 댓글 리스트
     */
    Optional<List<Reply>> listByMno(long mno);

}