package com.evaluation.persistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.QStaff;
import com.evaluation.domain.Staff;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface StaffRepository extends CrudRepository<Staff, Long>, QuerydslPredicateExecutor<Staff> {

	// relation 설정 시 리스트에 등록 안된 피평가자 출력하기 위한
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND s NOT IN (SELECT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.tno=:tno) ORDER BY s.sno ASC ")
	public Optional<List<Staff>> getStaffForEvaluated(@Param("cno") long cno, @Param("tno") long tno);

	// relation 설정 시 평가자 출력하기 위해, 해당 피평가자에 대한 평가자에 속하지 않은 사람. + 본인이 아닌 사람 중에
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND NOT s.sno=:sno AND s NOT IN (SELECT r.evaluator FROM Relation360 r WHERE r.rno>0 AND r.tno=:tno AND r.evaluated.sno=:sno) ORDER BY s.sno ASC ")
	public Optional<List<Staff>> getStaffForEvaluator(@Param("cno") long cno, @Param("tno") long tno,
			@Param("sno") long sno);

	// 직원 전원 삭제
	@Transactional
	@Modifying
	@Query("DELETE FROM Staff s WHERE s.cno=?1")
	public void deleteByCno(long cno);

	// xl파일로 relation 설정할 때 직원 불러오기 위해! evaluated 위해
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.email=:email")
	public Optional<Staff> findByEmail(@Param("email") String email);

	// xl파일로 relation 설정할 때 직원 불러오기 위해! evaluator 위해
	@Query("SELECT s FROM Staff s WHERE s.cno=?1 AND s.name=?2")
	public Optional<Staff> findByCnoAndName(long cno, String name);

	// xl파일로 다운 위해
	@Query("SELECT s FROM Staff s WHERE s.cno=:cno ORDER BY s.name ASC")
	public Optional<List<Staff>> findByCno(@Param("cno") Long cno);

	public default Predicate makePredicate(String type, String keyword, Long cno) {

		BooleanBuilder builder = new BooleanBuilder();

		QStaff staff = QStaff.staff;

		builder.and(staff.sno.gt(0));
		builder.and(staff.cno.eq(cno));

		if (type == null) {
			return builder;
		}

		switch (type) {
		case "email":
			builder.and(staff.email.like("%" + keyword + "%"));
			break;
		case "name":
			builder.and(staff.name.like("%" + keyword + "%"));
			break;
		case "department":
			builder.and(staff.department1.like("%" + keyword + "%"));
			builder.or(staff.department2.like("%" + keyword + "%"));
			break;
		case "division":
			builder.and(staff.division1.like("%" + keyword + "%"));
			builder.or(staff.division2.like("%" + keyword + "%"));
			break;
		case "level":
			builder.and(staff.level.like("%" + keyword + "%"));
			break;
		}
		return builder;
	}
}
