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

public interface LevelRepository extends CrudRepository<Level, Long>, QuerydslPredicateExecutor<Level> {

    // 직급 전체 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM Level l WHERE l.cno=?1")
    public void deleteByCno(long cno);

    //staff등록할 때 distinctInfo를 위한
    @Query("SELECT DISTINCT l.content FROM Level l WHERE cno=?1 ORDER BY l.content ASC")
    public List<String> getListLevel(long cno);

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
