package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationMBO;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface RelationMBOService {

    public void register(RelationMBO relationMBO);

    public Optional<RelationMBO> read(Long rno);

    public void modify(RelationMBO relationMBO);

    public void remove(Long rno);

    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    public Optional<List<RelationMBO>> findRelationByEvaulatedSno(long sno, long tno);

    public Optional<Staff> findInEvaluator(long tno, String email);

    public Optional<List<RelationMBO>> findByEvaluator(long sno, long tno);

    public Optional<List<RelationMBO>> findAllbyTno(long tno);

    public List<Staff> findDintinctEavluatedbyTno(long tno);

    public Optional<List<List<String>>> progressOfSurevey(long tno);
}