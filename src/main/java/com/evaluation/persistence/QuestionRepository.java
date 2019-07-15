package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.QQuestion;
import com.evaluation.domain.Question;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <code>QuestionRepository</code> 객체는 Question 객체의 영속화를 위해 표현한다.
 */
public interface QuestionRepository extends CrudRepository<Question, Long>, QuerydslPredicateExecutor<Question> {
    /**
     * 한 회차에 속하는 질문정보를 삭제한다.
     * 
     * @param tno 회차 id
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Question q WHERE q.tno=:tno")
    void deleteByTno(@Param("tno") long tno);

    /**
     * 사용자 페이지에서 피평가자의 직군 계층 정보에 따라 문항을 찾는다..
     * 
     * @param tno       회차id
     * @param division1 직군명칭
     * @param division2 계층명칭
     * @return 한 질문 정보 리스트의 리스트
     */
    @Query("SELECT q FROM Question q WHERE q.qno>0 AND q.tno=:tno AND q.division1=:division1 AND q.division2=:division2 ORDER BY q.idx ASC")
    Optional<List<Question>> getListByDivision(@Param("tno") Long tno, @Param("division1") String division1,
            @Param("division2") String division2);

    /**
     * 서베이 메인에 직군-계층 정보와 함께 각 문항 수를 표시한다.
     * 
     * @param tno 회차id
     * @return 직군-계층-문항수 리스트의 리스트
     */
    @Query("SELECT q.division1, q.division2, COUNT(q.qno) FROM Question q WHERE tno=:tno GROUP BY q.division1, q.division2")
    Optional<List<List<String>>> getDistinctDivisionCountByTno(@Param("tno") long tno);

    /**
     * 한 회차에 존재하는 모든 질문 정보를 찾는다. (엑셀 다운)
     * 
     * @param tno 회차 id
     * @return 질문 객체 리스트
     */
    @Query("SELECT q FROM Question q WHERE tno=:tno")
    Optional<List<Question>> findByTno(@Param("tno") long tno);

    /**
     * 개별 질문 등록 시 중복제거한 category를 찾는다.
     * 
     * @param tno 회차 id
     * @return 중복제거한 카테고리 리스트
     */
    @Query("SELECT DISTINCT q.category FROM Question q WHERE tno=:tno ORDER BY q.category ASC")
    List<String> getListCategory(@Param("tno") long tno);

    /**
     * @param type    검색을 위한 타입
     * @param keyword 검색 키워드
     * @param tno     회차id
     * @return querydsl을 사용해서 검색을 위한 builder를 리턴
     */
    default Predicate makePredicate(String type, String keyword, Long tno) {

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
