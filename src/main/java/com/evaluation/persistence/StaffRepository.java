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

/**
 * <code>StaffRepository</code> 객체는 Staff 객체의 영속화를 위해 표현한다.
 */
public interface StaffRepository extends CrudRepository<Staff, Long>, QuerydslPredicateExecutor<Staff> {

	/**
	 * 한 회사에서 한 회차에 속하는 본인 평가 관계정보에 없는 직원정보를 찾는다. (설정 시 펴핑가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @return 직원 객체 리스트
	 */
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND s NOT IN (SELECT r.evaluated FROM RelationSurvey r WHERE r.rno>0 AND r.tno=:tno AND r.relation='me') ORDER BY s.sno ASC ")
	public Optional<List<Staff>> get360EvaluatedList(@Param("cno") long cno, @Param("tno") long tno);

	/**
	 * 전 직원 중에 직원 정보가 동일하지 않고(본인이 아닌), 평가자에 속하지 않은 직원정보를 찾는다. (설정 시 평가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @param sno 직원id
	 * @return 직원 객체 리스트
	 */
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND NOT s.sno=:sno AND s NOT IN (SELECT r.evaluator FROM RelationSurvey r WHERE r.rno>0 AND r.tno=:tno AND r.evaluated.sno=:sno) ORDER BY s.sno ASC ")
	public Optional<List<Staff>> get360EvaluatorList(@Param("cno") long cno, @Param("tno") long tno,
			@Param("sno") long sno);

	/**
	 * 한 회사에서 한 회차에 속하는 본인 평가 관계정보에 없는 직원정보를 찾는다. (설정 시 펴핑가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @return 직원 객체 리스트
	 */
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND s NOT IN (SELECT r.evaluated FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.relation='me') ORDER BY s.sno ASC ")
	public Optional<List<Staff>> getMboEvaluatedList(@Param("cno") long cno, @Param("tno") long tno);

	/**
	 * 전 직원 중에 직원 정보가 동일하지 않고(본인이 아닌), 평가자에 속하지 않은 직원정보를 찾는다. (설정 시 평가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @param sno 직원id
	 * @return 직원 객체 리스트
	 */
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.cno=:cno AND NOT s.sno=:sno AND s NOT IN (SELECT r.evaluator FROM RelationMbo r WHERE r.rno>0 AND r.tno=:tno AND r.evaluated.sno=:sno) ORDER BY s.sno ASC ")
	public Optional<List<Staff>> getMboEvaluatorList(@Param("cno") long cno, @Param("tno") long tno,
			@Param("sno") long sno);

	/**
	 * 한 회사에 속하는 모든 직원정보를 삭제한다.
	 * 
	 * @param cno 회사id
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM Staff s WHERE s.cno=:cno")
	public void deleteByCno(@Param("cno") long cno);

	/**
	 * 이메일로 직원 정보를 찾는다. (엑셀 피평가자)
	 * 
	 * @param email 이메일
	 * @return 직원 객체 리스트
	 */
	@Query("SELECT s FROM Staff s WHERE s.sno>0 AND s.email=:email")
	public Optional<Staff> findByEmail(@Param("email") String email);

	/**
	 * 이름으로 직원 정보를 찾는다. (엑셀 평가자)
	 * 
	 * @param cno  회사id
	 * @param name 이름
	 * @return 직원 객체
	 */
	@Query("SELECT s FROM Staff s WHERE s.cno=?1 AND s.name=?2")
	public Optional<Staff> findByCnoAndName(long cno, String name);

	/**
	 * 한 회사에 속하는 모든 직원 정보를 찾는다.
	 * 
	 * @param cno 회사id
	 * @return 직원 객체 리스트
	 */
	@Query("SELECT s FROM Staff s WHERE s.cno=:cno ORDER BY s.name ASC")
	public Optional<List<Staff>> findByCno(@Param("cno") Long cno);

	/**
	 * @param type    검색을 위한 타입
	 * @param keyword 검색 키워드
	 * @param cno     회사id
	 * @return querydsl을 사용해서 검색을 위한 builder를 리턴
	 */
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
