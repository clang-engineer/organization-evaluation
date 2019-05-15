package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.Department;
import com.evaluation.domain.QDepartment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long>, QuerydslPredicateExecutor<Department> {
    @Query("SELECT d FROM Department d WHERE d.cno=?1 AND d.dno>0 ORDER BY d.dno ASC")
    public List<Department> getDepartmentOfCompany(long cno);

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
