package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationSurvey;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface RelationSurveyService {

    void register(RelationSurvey relationSurvey);

    Optional<RelationSurvey> read(Long rno);

    void modify(RelationSurvey relationSurvey);

    void remove(Long rno);

    Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    Optional<List<RelationSurvey>> findByEvaulated(long tno, long sno);

    Optional<Staff> findByEvaluatorEmail(long tno, String email);

    Optional<List<RelationSurvey>> findByEvaluator(long tno, long sno);

    Optional<List<RelationSurvey>> findAllByTno(long tno);

    List<Staff> findDintinctEavluatedByTno(long tno);

    Optional<List<List<String>>> progressOfSurevey(long tno);
}