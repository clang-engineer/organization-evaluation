package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.QRelation360;
import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface Relation360Repository
        extends CrudRepository<Relation360, Long>, QuerydslPredicateExecutor<Relation360> {

    // 하나의 tno에 존재하는 모든 relation distinct에서 관계별로 셀 나누기 위해 사용
    @Query("SELECT r FROM Relation360 r WHERE r.rno>0 AND r.tno=?1")
    public List<Relation360> findByTno(long tno);

    //모든 테이블 가져오니 넘 느려서, 페이지에 표시되는 관련 관계자 정보만 가져오기로 함.
    @Query("SELECT r FROM Relation360 r WHERE r.rno>0 AND r.evaluated.sno=?1")
    public List<Relation360> findByEvaulatedSno(long sno);

    // 하나의 tno에 존재하는 모든 relation 중 중복을 제거한 피평가자 실질적으로 페이징 처리됨.
    @Query("SELECT DISTINCT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.tno=?1 ORDER BY r.evaluated.sno DESC")
    public Page<Staff> getDistinctEvaluatedList(long tno, Pageable pageable);

    // ecaluated 이름으로 검색했을 때의 유일값!
    @Query("SELECT DISTINCT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.evaluated.name LIKE %?1% AND r.tno=?2")
    public Page<Staff> getDistinctEvaluatedListByEvaluated(String keyword, long tno, Pageable pageable);

    // ecaluator 이름으로 검색했을 때의 유일값!
    @Query("SELECT DISTINCT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.evaluator.name LIKE %?1% AND r.tno=?2")
    public Page<Staff> getDistinctEvaluatedListByEvaluator(String keyword, long tno, Pageable pageable);

    // 회차에 속하는 특정 evaluated정보 모두 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Relation360 r WHERE r.rno>0 AND r.tno=?1 AND r.evaluated.sno=?2")
    public void deleteEvaluatedInfo(long tno, long sno);

    // 회차에 속하는 모든 realtion 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Relation360 r WHERE r.rno>0 AND r.tno=?1")
    public void deleteAllRelationByTno(long tno);

    public default Predicate makePredicate(String type, String keyword, Long tno) {

        BooleanBuilder builder = new BooleanBuilder();

        QRelation360 relation360 = QRelation360.relation360;

        builder.and(relation360.rno.gt(0));
        builder.and(relation360.tno.eq(tno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "evaluated":
            builder.and(relation360.evaluated.email.like("%" + keyword + "%"));
            break;
        case "evaluator":
            builder.and(relation360.evaluator.email.like("%" + keyword + "%"));
            break;
        case "relation":
            builder.and(relation360.relation.like("%" + keyword + "%"));
            break;
        case "finish":
            builder.and(relation360.finish.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }

}