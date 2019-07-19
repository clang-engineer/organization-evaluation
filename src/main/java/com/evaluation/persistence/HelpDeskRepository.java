package com.evaluation.persistence;

import com.evaluation.domain.HelpDesk;
import com.evaluation.domain.QHelpDesk;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * HelpRepository
 */
public interface HelpDeskRepository extends CrudRepository<HelpDesk, Long>, QuerydslPredicateExecutor<HelpDesk> {

    /**
     * 
     * @param type    검색을 위한 타입
     * @param keyword 검색 키워드
     * @return querydsl을 사용해서 검색을 위한 builder를 리턴
     */
    default Predicate makePredicate(String type, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        QHelpDesk helpDesk = QHelpDesk.helpDesk;

        builder.and(helpDesk.hno.gt(0));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "surveyInfo":
            builder.and(helpDesk.surveyInfo.like("%" + keyword + "%"));
            break;
        case "telephone":
            builder.and(helpDesk.telephone.like("%" + keyword + "%"));
            break;
        case "content":
            builder.and(helpDesk.content.like("%" + keyword + "%"));
            break;
        case "complete":
            builder.and(helpDesk.complete.like("%" + keyword + "%"));
            break;
        }
        return builder;
    }
}