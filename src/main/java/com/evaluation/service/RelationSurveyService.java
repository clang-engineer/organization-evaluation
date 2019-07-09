package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationSurvey;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>RelationSurveyService</code> 객체는 RelationSurvey객체 sevice 계층의 interface를
 * 표현한다.
 */
public interface RelationSurveyService {

    /**
     * survey 관계 정보를 등록한다.
     * 
     * @param relationSurvey survey 관계정보
     */
    void register(RelationSurvey relationSurvey);

    /**
     * survey 관계 정보를 찾는다.
     * 
     * @param rno 관계 id
     * @return survey 관계 정보
     */
    Optional<RelationSurvey> read(Long rno);

    /**
     * survey 관계 정보를 수정한다.
     * 
     * @param relationSurvey survey 관계정보
     */
    void modify(RelationSurvey relationSurvey);

    /**
     * survey 관계 정보를 삭제한다.
     * 
     * @param rno 관계 id
     */
    void remove(Long rno);

    /**
     * 하나의 회차에 존재하는 모든 관계 중 중복을 제거한 피평가자 리스트를 찾는다.
     * 
     * @param tno 회차id
     * @param vo  페이지 정보
     * @return 중복제거한 피평가자 정보 리스트
     */
    Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    /**
     * 한 회차에 속하는 한 피평가자의 모든 관계 정보를 찾는다.
     * 
     * @param tno 회차id
     * @param sno 직원id
     * @return Survey 관계 정보 리스트
     */
    Optional<List<RelationSurvey>> findByEvaulated(long tno, long sno);

    /**
     * 회차에 속하는 평가자 중에 이메일 정보가 존재하는지 찾는다.(로그인 시)
     * 
     * @param tno   회차id
     * @param email 직원 email
     * @return 직원 객체
     */
    Optional<Staff> findByEvaluatorEmail(long tno, String email);

    /**
     * 한 회차에 속하는 평가자의 모든 관계 정보를 찾는다.
     * 
     * @param tno 회차 id
     * @param sno 직원 id
     * @return Survey 관계 객체 리스트
     */
    Optional<List<RelationSurvey>> findByEvaluator(long tno, long sno);

    /**
     * 회차에 속하는 모든 관계 정보를 찾는다.(엑셀 다운)
     * 
     * @param tno 회차id
     * @return Survey 관계정보 객체 리스트
     */
    Optional<List<RelationSurvey>> findAllByTno(long tno);

    /**
     * 한 회차에 속하는 중복 제거한 모든 피평가자를 찾는다. (xl 다운로드 시 이용)
     * 
     * @param tno 회차id
     * @return 직원 객체 리스트
     */
    List<Staff> findDintinctEavluatedByTno(long tno);

    /**
     * 전 직원의 평가 진행율을 찾는다.
     * 
     * @param tno 회차 id
     * @return 평가 진행율을 담은 리스트의 리스트
     */
    Optional<List<List<String>>> progressOfSurevey(long tno);
}