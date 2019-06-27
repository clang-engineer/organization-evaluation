package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.QRelationMBO;
import com.evaluation.domain.RelationMBO;
import com.evaluation.domain.Staff;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RelationMBORepository
        extends CrudRepository<RelationMBO, Long>, QuerydslPredicateExecutor<RelationMBO> {

    // 모든 테이블 가져오니 넘 느려서, 페이지에 표시되는 관련 관계자 정보만 가져오기로 함.
    @Query("SELECT r FROM RelationMBO r WHERE r.rno>0 AND r.evaluated.sno=?1 AND r.tno=?2")
    public Optional<List<RelationMBO>> findByEvaulatedSno(long sno, long tno);

    /* criteria 처리 필요 */
    // 하나의 tno에 존재하는 모든 relation 중 중복을 제거한 피평가자 실질적으로 페이징 처리됨.
    @Query("SELECT DISTINCT r.evaluated FROM RelationMBO r WHERE r.rno>0 AND r.tno=?1 ORDER BY r.evaluated.sno ASC")
    public Page<Staff> getDistinctEvaluatedList(long tno, Pageable pageable);

    // ecaluated 이름으로 검색했을 때의 유일값!
    @Query("SELECT DISTINCT r.evaluated FROM RelationMBO r WHERE r.rno>0 AND r.evaluated.name LIKE %?1% AND r.tno=?2")
    public Page<Staff> getDistinctEvaluatedListByEvaluated(String keyword, long tno, Pageable pageable);

    // ecaluator 이름으로 검색했을 때의 유일값!
    @Query("SELECT DISTINCT r.evaluated FROM RelationMBO r WHERE r.rno>0 AND r.evaluator.name LIKE %?1% AND r.tno=?2")
    public Page<Staff> getDistinctEvaluatedListByEvaluator(String keyword, long tno, Pageable pageable);
    /* ./criteria 처리 필요 */

    // 로그인 할 때 사용 회차에 있는 평가자면 로그인
    @Query("SELECT DISTINCT r.evaluator FROM RelationMBO r WHERE r.tno=?1 AND r.evaluator.email=?2")
    public Optional<Staff> findInEvaluator(long tno, String email);

    // 로그인 식 출력되는 피평가자 리스트.
    @Query("SELECT r FROM RelationMBO r WHERE r.rno>0 AND r.evaluator.sno=?1 AND r.tno=?2")
    public Optional<List<RelationMBO>> findByEvaulaordSno(long sno, long tno);

    // xl 다운로드를 위한 turn에 속하는 전체 관계
    @Query("SELECT r FROM RelationMBO r WHERE r.rno>0 AND r.tno=:tno")
    public Optional<List<RelationMBO>> findAllbyTno(@Param("tno") Long tno);

    // xl 다운로드를 위한 turn에 속하는 중복제거 피평가자, Optional로는 변환 오류 발생. Distinct는 안되는 듯.? 위에
    // 유사함수 있지만 페이지 처리 때문에 따로 만듬.
    @Query("SELECT DISTINCT r.evaluated FROM RelationMBO r WHERE r.rno>0 AND r.tno=:tno")
    public List<Staff> findDintinctEavluatedbyTno(@Param("tno") Long tno);

    // 평가현황을 위한 쿼리 count if 부분이 해결이 안돼서 native쿼리를 사용함.
    @Query(value = "SELECT s.name, s.email, s.level, s.department1, s.department2, count(if(finish='Y',rno,null)) as complete,count(*) as total, (count(if(finish='Y',rno,null))/count(*))*100 as ratio FROM tbl_relationmbo as r left join tbl_staff as s on r.evaluator=s.sno where r.turn_tno=:tno group by evaluator ORDER BY s.name ASC", nativeQuery = true)
    public Optional<List<List<String>>> progressOfSurevey(@Param("tno") long tno);

    // 상사 평가시 본인 평가 정보를 확인하기 위한 쿼리
    @Query("SELECT r FROM RelationMBO r WHERE r.rno>0 AND r.relation='me' AND r.tno=:tno AND r.evaluated.sno=:sno")
    public Optional<RelationMBO> findMeRelationByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

    public default Predicate makePredicate(String type, String keyword, Long tno) {

        BooleanBuilder builder = new BooleanBuilder();

        QRelationMBO relationMBO = QRelationMBO.relationMBO;

        builder.and(relationMBO.rno.gt(0));
        builder.and(relationMBO.tno.eq(tno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "evaluated":
            builder.and(relationMBO.evaluated.email.like("%" + keyword + "%"));
            break;
        case "evaluator":
            builder.and(relationMBO.evaluator.email.like("%" + keyword + "%"));
            break;
        case "relation":
            builder.and(relationMBO.relation.like("%" + keyword + "%"));
            break;
        case "finish":
            builder.and(relationMBO.finish.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }

}