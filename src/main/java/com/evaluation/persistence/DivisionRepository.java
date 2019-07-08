package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.Division;
import com.evaluation.domain.QDivision;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <code>DivisionRepository</code> 객체는 Division 객체의 영속화를 위해 표현한다.
 */
public interface DivisionRepository extends CrudRepository<Division, Long>, QuerydslPredicateExecutor<Division> {

    /**
     * 회사에 속하는 전체 직군 계층 정보를 삭제한다.
     * 
     * @param cno 회사 id
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Division d WHERE d.cno=?1")
    public void deleteByCno(long cno);

    /**
     * 직원 개별 등록할 때 중복제거한 직군 정보 위해
     * 
     * @param cno 회사 id
     * @return
     */
    @Query("SELECT DISTINCT d.division1 FROM Division d WHERE cno=?1 ORDER BY d.division1 ASC")
    public List<String> getListDivision1(long cno);

    /**
     * 직원 개별 등록할 때 중복제거한 계층 정보 위해
     * 
     * @param cno 회사 id
     * @return
     */
    @Query("SELECT DISTINCT d.division2 FROM Division d WHERE cno=?1 ORDER BY d.division2 ASC")
    public List<String> getListDivision2(long cno);

    /**
     * @param type    검색을 위한 타입
     * @param keyword 검색 키워드
     * @param cno     회사id
     * @return querydsl을 사용해서 검색을 위한 builder를 리턴
     */
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
