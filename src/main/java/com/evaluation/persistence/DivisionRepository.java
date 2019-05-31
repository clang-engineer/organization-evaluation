package com.evaluation.persistence;

import com.evaluation.domain.Division;
import com.evaluation.domain.QDivision;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DivisionRepository extends CrudRepository<Division, Long>, QuerydslPredicateExecutor<Division> {

    // 구분 전체 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM Division d WHERE d.cno=?1")
    public void deleteByCno(long cno);

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
