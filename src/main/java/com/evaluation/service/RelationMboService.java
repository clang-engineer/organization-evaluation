package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface RelationMboService {

    void register(RelationMbo relationMbo);

    Optional<RelationMbo> read(Long rno);

    void modify(RelationMbo relationMbo);

    void remove(Long rno);

    Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    Optional<List<RelationMbo>> findByEvaulated(long tno, long sno);

    Optional<Staff> findByEvaluatorEmail(long tno, String email);

    Optional<List<RelationMbo>> findByEvaluator(long tno, long sno);

    Optional<List<RelationMbo>> findAllByTno(long tno);

    List<Staff> findDintinctEavluatedByTno(long tno);

    Optional<List<List<String>>> progressOfSurevey(long tno);

    Optional<RelationMbo> findMeRelationByTnoSno(long tno, long sno);

    Optional<List<List<String>>> progressOfPlan(long tno);
}