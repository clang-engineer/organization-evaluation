package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface RelationSurveyService {

    public void register(Relation360 relation360);

    public Optional<Relation360> read(Long rno);

    public void modify(Relation360 relation360);

    public void remove(Long rno);

    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    public Optional<List<Relation360>> findByEvaulated(long tno, long sno);

    public Optional<Staff> findByEvaluatorEmail(long tno, String email);

    public Optional<List<Relation360>> findByEvaluator(long tno, long sno);

    public Optional<List<Relation360>> findAllByTno(long tno);

    public List<Staff> findDintinctEavluatedByTno(long tno);

    public Optional<List<List<String>>> progressOfSurevey(long tno);
}