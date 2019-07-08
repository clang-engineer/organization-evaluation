package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Reply;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * <code>ReplyRepository</code> 객체는 Reply 객체의 영속화를 위해 표현한다.
 */
public interface ReplyRepository extends CrudRepository<Reply, Long> {

    /**
     * 특정 목표에 달린 댓글을 찾는다.
     * 
     * @param mno 목표id
     * @return 댓글 객체 리스트
     */
    @Query("SELECT r FROM Reply r WHERE r.mno=:mno")
    public Optional<List<Reply>> listByMno(@Param("mno") long mno);
}