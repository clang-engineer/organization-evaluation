package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.Level;
import com.evaluation.domain.QLevel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <code>LevelRepository</code> 객체는 Level 객체의 영속화를 위해 표현한다.
 */
public interface LevelRepository extends CrudRepository<Level, Long>, QuerydslPredicateExecutor<Level> {

    /**
     * 한 회사에 속하는 직급 정보 전체 삭제
     * 
     * @param cno 회사id
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Level l WHERE l.cno=?1")
    public void deleteByCno(long cno);

    /**
     * 직원 정보 등록할 때 중복제거한 직급 정보를 찾는다.
     * 
     * @param cno 회사 id
     * @return 중복제거한 직급 리스트
     */
    @Query("SELECT DISTINCT l.content FROM Level l WHERE cno=?1 ORDER BY l.content ASC")
    public List<String> getListLevel(long cno);

    /**
     * @param type    검색을 위한 타입
     * @param keyword 검색 키워드
     * @param cno     회사id
     * @return querydsl을 사용해서 검색을 위한 builder를 리턴
     */
    public default Predicate makePredicate(String type, String keyword, long cno) {

        BooleanBuilder builder = new BooleanBuilder();

        QLevel level = QLevel.level;

        builder.and(level.lno.gt(0));
        builder.and(level.cno.eq(cno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "level":
            builder.and(level.content.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }
}
