package com.evaluation.persistence;

import java.util.Optional;

import com.evaluation.domain.Company;
import com.evaluation.domain.QCompany;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * <code>CompanyRepository</code> 객체는 Company 객체의 영속화를 위해 표현한다.
 */
public interface CompanyRepository extends CrudRepository<Company, Long>, QuerydslPredicateExecutor<Company> {

	/**
	 * company id로 회사 정보 조회할 때 사용
	 * 
	 * @param id 회사id (cno가 아닌 등록한 회사 고유 id)
	 * @return 회사정보
	 */
	@Query("SELECT c FROM Company c WHERE c.cno>0 AND c.id=:id")
	Optional<Company> findByCompanyId(@Param("id") String id);

	/**
	 * 
	 * @param type 검색을 위한 타입
	 * @param keyword 검색 키워드
	 * @return querydsl을 사용해서 검색을 위한 builder를 리턴
	 */
	default Predicate makePredicate(String type, String keyword) {

		BooleanBuilder builder = new BooleanBuilder();

		QCompany company = QCompany.company;

		builder.and(company.cno.gt(0));

		if (type == null) {
			return builder;
		}

		switch (type) {
		case "id":
			builder.and(company.id.like("%" + keyword + "%"));
			break;
		case "name":
			builder.and(company.name.like("%" + keyword + "%"));
			break;
		}
		return builder;
	}
}
