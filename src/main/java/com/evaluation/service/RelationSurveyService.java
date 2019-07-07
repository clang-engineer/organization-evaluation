package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationSurvey;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface RelationSurveyService {

    public void register(RelationSurvey relationSurvey);

    public Optional<RelationSurvey> read(Long rno);

    public void modify(RelationSurvey relationSurvey);

    public void remove(Long rno);

    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    public Optional<List<RelationSurvey>> findByEvaulated(long tno, long sno);

    public Optional<Staff> findByEvaluatorEmail(long tno, String email);

    public Optional<List<RelationSurvey>> findByEvaluator(long tno, long sno);

    public Optional<List<RelationSurvey>> findAllByTno(long tno);

    public List<Staff> findDintinctEavluatedByTno(long tno);

    public Optional<List<List<String>>> progressOfSurevey(long tno);
}