package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Department;
import com.evaluation.domain.QDepartment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DepartmentRepository extends CrudRepository<Department, Long>, QuerydslPredicateExecutor<Department> {

    // 팀별 목표 설정하기 위해 팀장인 팀이 있는지 확인
    @Query("SELECT d FROM Department d WHERE d.cno=:cno AND d.leader.sno=:sno")
    public Optional<List<Department>> findByCnoSno(@Param("cno") long cno, @Param("sno") long sno);

    // 부서정보 전체 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM Department d WHERE d.cno=?1")
    public void deleteByCno(long cno);

    public default Predicate makePredicate(String type, String keyword, long cno) {

        BooleanBuilder builder = new BooleanBuilder();

        QDepartment department = QDepartment.department;

        builder.and(department.dno.gt(0));
        builder.and(department.cno.eq(cno));

        if (type == null) {
            return builder;
        }

        switch (type) {
        case "department1":
            builder.and(department.department1.like("%" + keyword + "%"));
            break;
        case "department2":
            builder.and(department.department2.like("%" + keyword + "%"));
            break;
        }

        return builder;
    }
}
