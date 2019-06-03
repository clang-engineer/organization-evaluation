package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.Turn;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TurnRepository extends CrudRepository<Turn, Long> {

	@Query("SELECT t FROM Turn t WHERE t.cno=?1 AND t.tno>0 ORDER BY t.tno ASC")
	public List<Turn> getTurnsOfCompany(Long cno);

	@Modifying
	@Transactional
	@Query("DELETE FROM Turn t WHERE t.cno=?1")
	public void deleteByCno(long cno);
}
