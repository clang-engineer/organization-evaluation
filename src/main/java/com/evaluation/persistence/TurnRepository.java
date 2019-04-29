package com.evaluation.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;

public interface TurnRepository extends CrudRepository<Turn, Long> {

	@Query("SELECT t FROM Turn t WHERE t.company=?1 AND t.tno>0 ORDER BY t.tno DESC")
	public List<Turn> getTurnsOfCompany(Company company);
}
