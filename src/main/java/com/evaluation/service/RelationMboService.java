package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface RelationMboService {

    public void register(RelationMbo relationMbo);

    public Optional<RelationMbo> read(Long rno);

    public void modify(RelationMbo relationMbo);

    public void remove(Long rno);

    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    public Optional<List<RelationMbo>> findByEvaulated(long tno, long sno);

    public Optional<Staff> findByEvaluatorEmail(long tno, String email);

    public Optional<List<RelationMbo>> findByEvaluator(long tno, long sno);

    public Optional<List<RelationMbo>> findAllByTno(long tno);

    public List<Staff> findDintinctEavluatedByTno(long tno);

    public Optional<List<List<String>>> progressOfSurevey(long tno);

    public Optional<RelationMbo> findMeRelationByTnoSno(long tno, long sno);

    public Optional<List<List<String>>> progressOfPlan(long tno);
}