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

/**
 * <code>DepartmentRepository</code> 객체는 Department 객체의 영속화를 위해 표현한다.
 */
public interface DepartmentRepository extends CrudRepository<Department, Long>, QuerydslPredicateExecutor<Department> {

    /**
     * 팀별 목표 설정하기 위해 직원id가 팀장인 팀이 있는지 확인
     * 
     * @param tno 회차 id
     * @param sno 리더에 해당하는 직원의 id
     * @return 부서 리스트
     */
    @Query("SELECT d FROM Department d WHERE d.tno=:tno AND d.leader.sno=:sno")
    Optional<List<Department>> findByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

    /**
     * 직원등록할 때 중복제거한 부서정보를 찾는다.
     * 
     * @param tno 회차 id
     * @return 중복제거한 부서1 리스트
     */
    @Query("SELECT DISTINCT d.department1 FROM Department d WHERE tno=:tno ORDER BY d.department1 ASC")
    List<String> getListDepartment1(@Param("tno") long tno);

    /**
     * 직원등록할 때 중복제거한 부서정보를 찾는다.
     * 
     * @param tno 회차 id
     * @return 중복제거한 부서2 리스트
     */
    @Query("SELECT DISTINCT d.department2 FROM Department d WHERE tno=:tno ORDER BY d.department2 ASC")
    public List<String> getListDepartment2(@Param("tno") long tno);

    /**
     * 회차에 속하는 부서정보 전체 삭제
     * 
     * @param tno 회차 id
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Department d WHERE d.tno=:tno")
    public void deleteByTno(@Param("tno") long tno);

    /**
     * 부문명과 부서명으로 팀장이 설정한 팀 목표를 찾는다.
     * 
     * @param tno         회차 id
     * @param department1 부문병
     * @param department2 부서명
     * @return
     */
    @Query("SELECT d FROM Department d WHERE d.tno=:tno AND d.department1=:department1 AND d.department2=:department2")
    Optional<Department> findByDeparment(@Param("tno") long tno, @Param("department1") String department1,
            @Param("department2") String department2);

    /**
     * @param type    검색을 위한 타입
     * @param keyword 검색 키워드
     * @param tno     회차id
     * @return querydsl을 사용해서 검색을 위한 builder를 리턴
     */
    default Predicate makePredicate(String type, String keyword, long tno) {

        BooleanBuilder builder = new BooleanBuilder();

        QDepartment department = QDepartment.department;

        builder.and(department.dno.gt(0));
        builder.and(department.tno.eq(tno));

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
