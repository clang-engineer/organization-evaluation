package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.QRelation360;
import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface Relation360Repository
        extends CrudRepository<Relation360, Long>, QuerydslPredicateExecutor<Relation360> {

    @Query("SELECT r FROM Relation360 r WHERE r.rno>0 AND r.tno=?1")
    public List<Relation360> findByTno(long tno);

    @Query("SELECT DISTINCT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.tno=?1")
    public Page<Staff> getDistinctEvaluatedList(long tno, Pageable pageable);

    @Query("SELECT DISTINCT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.evaluated.name LIKE %?1% AND r.tno=?2")
    public Page<Staff> getDistinctEvaluatedListByEvaluated(String keyword, long tno, Pageable pageable);

    @Query("SELECT DISTINCT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.evaluator.name LIKE %?1% AND r.tno=?2")
    public Page<Staff> getDistinctEvaluatedListByEvaluator(String keyword, long tno, Pageable pageable);

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