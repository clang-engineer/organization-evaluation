package com.evaluation.persistence;

import com.evaluation.domain.QRelation360;
import com.evaluation.domain.Relation360;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface Relation360Repository
        extends CrudRepository<Relation360, Long>, QuerydslPredicateExecutor<Relation360> {

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