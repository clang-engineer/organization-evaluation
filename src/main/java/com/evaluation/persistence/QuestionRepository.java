package com.evaluation.persistence;

import com.evaluation.domain.QQuestion;
import com.evaluation.domain.Question;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface QuestionRepository extends CrudRepository<Question, Long>, QuerydslPredicateExecutor<Question> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Question q WHERE q.tno=?1")
    public void deleteByTno(long tno);

    public default Predicate makePredicate(String type, String keyword, Long tno) {

        BooleanBuilder builder = new BooleanBuilder();

        QQuestion question = QQuestion.question;

        builder.and(question.qno.gt(0));
        builder.and(question.tno.eq(tno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "division":
            builder.and(question.division1.like("%" + keyword + "%"));
            builder.or(question.division2.like("%" + keyword + "%"));
            break;
        case "category":
            builder.and(question.category.like("%" + keyword + "%"));
            break;
        case "item":
            builder.and(question.item.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }
}
