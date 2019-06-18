package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Reply;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends CrudRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.mno=:mno")
    public Optional<List<Reply>> listByMno(@Param("mno") long mno);
}