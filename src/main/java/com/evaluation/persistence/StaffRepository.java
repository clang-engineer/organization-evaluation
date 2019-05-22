package com.evaluation.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.evaluation.domain.QStaff;
import com.evaluation.domain.Staff;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface StaffRepository extends CrudRepository<Staff, Long>, QuerydslPredicateExecutor<Staff> {

	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno ORDER BY s.name ASC")
	public List<Staff> getAllStaffListByCno(@Param("cno") long cno);

	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND s NOT IN (SELECT r.evaluated FROM Relation360 r WHERE r.rno>0 AND r.tno=:tno) ORDER BY s.sno ASC ")
	public List<Staff> getStaffForEvaluated(@Param("cno") long cno, @Param("tno") long tno);
	
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND s NOT IN (SELECT r.evaluator FROM Relation360 r WHERE r.rno>0 AND r.tno=:tno AND r.evaluated.sno=:sno) ORDER BY s.sno ASC ")
	public List<Staff> getStaffForEvaluator(@Param("cno") long cno, @Param("tno") long tno, @Param("sno") long sno);

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
