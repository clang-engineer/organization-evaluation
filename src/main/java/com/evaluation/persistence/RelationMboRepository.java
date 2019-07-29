package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.QRelationMbo;
import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * <code>RelationMboRepository</code> 객체는 RelationMbo 객체의 영속화를 위해 표현한다.
 */
public interface RelationMboRepository
        extends CrudRepository<RelationMbo, Long>, QuerydslPredicateExecutor<RelationMbo> {

    /**
     * 한 회차에 속하는 한 피평가자의 모든 관계 정보를 찾는다.
     * 
     * @param tno 회차id
     * @param sno 직원id
     * @return Mbo 관계 정보 리스트
     */
    @Query("SELECT r FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.evaluated.sno=:sno")
    Optional<List<RelationMbo>> findByEvaulated(@Param("tno") long tno, @Param("sno") long sno);

    /* criteria 처리 필요 */
    /**
     * 하나의 회차에 존재하는 모든 관계 중 중복을 제거한 피평가자 리스트를 찾는다.
     * 
     * @param tno      회차id
     * @param pageable 페이지 정보
     * @return 중복제거한 피평가자 정보 리스트
     */
    @Query("SELECT DISTINCT r.evaluated FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno ORDER BY r.evaluated.sno ASC")
    Page<Staff> getDistinctEvaluatedList(@Param("tno") long tno, Pageable pageable);

    /**
     * 피평가자 이름으로 검색했을 때의 중복 제거한 피평가자 리스트를 찾는다.
     * 
     * @param tno      회차id
     * @param keyword  검색keyword
     * @param pageable 페이지 정보
     * @return 중복제거한 피평가자 정보 리스트
     */
    @Query("SELECT DISTINCT r.evaluated FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.evaluated.name LIKE %:keyword%")
    Page<Staff> getDistinctEvaluatedListByEvaluated(@Param("tno") long tno, @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * 평가자 이름으로 검색했을 때의 중복 제거한 피평가자 리스트를 찾는다.
     * 
     * @param tno      회차id
     * @param keyword  검색keyword
     * @param pageable 페이지 정보
     * @return 중복제거한 피평가자 정보 리스트
     */
    @Query("SELECT DISTINCT r.evaluated FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.evaluator.name LIKE %:keyword%")
    Page<Staff> getDistinctEvaluatedListByEvaluator(@Param("tno") long tno, @Param("keyword") String keyword,
            Pageable pageable);
    /* ./criteria 처리 필요 */

    /**
     * 회차에 속하는 평가자 중에 이메일 정보가 존재하는지 찾는다.(로그인 시)
     * 
     * @param tno   회차id
     * @param email 직원 email
     * @return 직원 객체
     */
    @Query("SELECT DISTINCT r.evaluator FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.evaluator.email=:email")
    Optional<Staff> findByEvaluatorEmail(@Param("tno") long tno, @Param("email") String email);

    /**
     * 한 회차에 속하는 평가자의 모든 관계 정보를 찾는다.
     * 
     * @param tno 회차 id
     * @param sno 직원 id
     * @return Mbo관계 객체 리스트
     */
    @Query("SELECT r FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.evaluator.sno=:sno")
    Optional<List<RelationMbo>> findByEvaulator(@Param("tno") long tno, @Param("sno") long sno);

    /**
     * 회차에 속하는 모든 관계 정보를 찾는다.(엑셀 다운)
     * 
     * @param tno 회차id
     * @return mbo관계정보 객체 리스트
     */
    @Query("SELECT r FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno")
    Optional<List<RelationMbo>> findAllByTno(@Param("tno") Long tno);

    /**
     * 한 회차에 속하는 중복 제거한 모든 피평가자를 찾는다. (xl 다운로드 시 이용)
     * 
     * @param tno 회차id
     * @return 직원 객체 리스트
     */
    @Query("SELECT DISTINCT r.evaluated FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno ORDER BY r.evaluated.sno ASC")
    List<Staff> findDintinctEavluatedByTno(@Param("tno") Long tno);

    /**
     * See 단계에서 전 직원의 평가 진행율을 찾는다.
     * 
     * @param tno 회차 id
     * @return 평가 진행율을 담은 리스트의 리스트
     */
    @Query(value = "SELECT s.name, s.email, s.level, s.department1, s.department2, count(if(finish='Y',rno,null)) as complete,count(*) as total, (count(if(finish='Y',rno,null))/count(*)) as ratio, s.sno FROM tbl_relation_mbo as r left join tbl_staff as s on r.evaluator=s.sno where r.turn_tno=:tno group by evaluator ORDER BY s.name ASC", nativeQuery = true)
    Optional<List<List<String>>> progressOfSurevey(@Param("tno") long tno);

    /**
     * Plan, Do단계에서 전 직원의 목표 작성 비율을 찾는다.
     * 
     * @param tno 회차id
     * @return 목표 작성 비율을 담은 리스트의 리스트
     */
    @Query(value = "select s.sno, s.name, s.email, s.level, s.department1, s.department2, sum(if(m.finish='Y' and m.turn_tno=:tno,m.ratio,null)) from tbl_relation_mbo r left join tbl_mbo m on r.evaluated = m.staff_sno left join tbl_staff s on r.evaluated= s.sno where r.relation='me' and r.turn_tno=:tno group by r.evaluated order by s.name asc", nativeQuery = true)
    Optional<List<List<String>>> progressOfPlan(@Param("tno") long tno);

    /**
     * 한 회차에서 특정 인원의 본인평가 정보를 찾는다.(상사 평가시 본인 평가 정보를 확인하기 위한 쿼리)
     * 
     * @param tno 회차id
     * @param sno 직원 id
     * @return mbo관계 객체
     */
    @Query("SELECT r FROM RelationMbo r WHERE r.rno>0 AND r.relation='me' AND r.tno=:tno AND r.evaluated.sno=:sno")
    Optional<RelationMbo> findMeRelationByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

    /**
     * @param type    검색을 위한 타입
     * @param keyword 검색 키워드
     * @param tno     회차id
     * @return querydsl을 사용해서 검색을 위한 builder를 리턴
     */
    default Predicate makePredicate(String type, String keyword, Long tno) {

        BooleanBuilder builder = new BooleanBuilder();

        QRelationMbo relationMbo = QRelationMbo.relationMbo;

        builder.and(relationMbo.rno.gt(0));
        builder.and(relationMbo.tno.eq(tno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "evaluated":
            builder.and(relationMbo.evaluated.email.like("%" + keyword + "%"));
            break;
        case "evaluator":
            builder.and(relationMbo.evaluator.email.like("%" + keyword + "%"));
            break;
        case "relation":
            builder.and(relationMbo.relation.like("%" + keyword + "%"));
            break;
        case "finish":
            builder.and(relationMbo.finish.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }

}