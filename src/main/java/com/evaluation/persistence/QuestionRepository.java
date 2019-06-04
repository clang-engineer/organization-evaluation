package com.evaluation.persistence;

import java.util.List;

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

    // 서베이 메인에 직군-계층 별 문항 수 표시하기 위한 쿼리
    @Query("SELECT q.division1, q.division2, COUNT(q.qno) FROM Question q WHERE tno=?1 GROUP BY q.division1, q.division2")
    public List<List<String>> getDistinctDivisionCountByTno(long tno);

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
