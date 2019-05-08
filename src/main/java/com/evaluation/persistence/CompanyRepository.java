package com.evaluation.persistence;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.evaluation.domain.Company;
import com.evaluation.domain.QCompany;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface CompanyRepository extends CrudRepository<Company, Long>, QuerydslPredicateExecutor<Company> {

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
