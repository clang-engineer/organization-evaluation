package com.evaluation.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import com.evaluation.domain.Company;
import com.evaluation.domain.QCompany;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface CompanyRepository extends CrudRepository<Company, Long>, QuerydslPredicateExecutor<Company> {

	// company id로 회사 정보 조회할 때 사용
	@Query("SELECT c FROM Company c WHERE c.cno>0 AND c.id=?1")
	public Optional<Company> findByCompanyId(String name);

	public default Predicate makePredicate(String type, String keyword) {

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
