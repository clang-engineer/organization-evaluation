package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>RelationMboService</code> 객체는 RelationMbo객체 sevice 계층의 interface를 표현한다.
 */
public interface RelationMboService {
    /**
     * mbo 관계 정보를 등록한다.
     * 
     * @param relationMbo mbo 관계 정보
     */
    void register(RelationMbo relationMbo);

    /**
     * mbo 관계 정보를 찾는다.
     * 
     * @param rno 관계 id
     * @return mbo 관계 정보
     */
    Optional<RelationMbo> read(Long rno);

    /**
     * mbo 관계 정보를 수정한다.
     * 
     * @param relationMbo mbo 관계 정보
     */
    void modify(RelationMbo relationMbo);

    /**
     * mbo 관계 정보를 삭제한다.
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
     * @return Mbo 관계 정보 리스트
     */
    Optional<List<RelationMbo>> findByEvaulated(long tno, long sno);

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
     * @return Mbo관계 객체 리스트
     */
    Optional<List<RelationMbo>> findByEvaluator(long tno, long sno);

    /**
     * 회차에 속하는 모든 관계 정보를 찾는다.(엑셀 다운)
     * 
     * @param tno 회차id
     * @return mbo관계정보 객체 리스트
     */
    Optional<List<RelationMbo>> findAllByTno(long tno);

    /**
     * 한 회차에 속하는 중복 제거한 모든 피평가자를 찾는다. (xl 다운로드 시 이용)
     * 
     * @param tno 회차id
     * @return 직원 객체 리스트
     */
    List<Staff> findDintinctEavluatedByTno(long tno);

    /**
     * See 단계에서 전 직원의 평가 진행율을 찾는다.
     * 
     * @param tno 회차 id
     * @return 평가 진행율을 담은 리스트의 리스트
     */
    Optional<List<List<String>>> progressOfSurevey(long tno);

    /**
     * Plan, Do단계에서 전 직원의 목표 작성 비율을 찾는다.
     * 
     * @param tno 회차id
     * @return 목표 작성 비율을 담은 리스트의 리스트
     */
    Optional<List<List<String>>> progressOfPlan(long tno);

    /**
     * 한 회차에서 특정 인원의 본인평가 정보를 찾는다.(상사 평가시 본인 평가 정보를 확인하기 위한 쿼리)
     * 
     * @param tno 회차id
     * @param sno 직원 id
     * @return mbo관계 객체
     */
    Optional<RelationMbo> findMeRelationByTnoSno(long tno, long sno);
}