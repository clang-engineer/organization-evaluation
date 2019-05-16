package com.evaluation.persistence;

import com.evaluation.domain.Division;
import com.evaluation.domain.QDivision;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DivisionRepository extends CrudRepository<Division, Long>, QuerydslPredicateExecutor<Division> {

    public default Predicate makePredicate(String type, String keyword, long cno) {

        BooleanBuilder builder = new BooleanBuilder();

        QDivision division = QDivision.division;

        builder.and(division.dno.gt(0));
        builder.and(division.cno.eq(cno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "division1":
            builder.and(division.division1.like("%" + keyword + "%"));
            break;
        case "division2":
            builder.and(division.division2.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }
}
