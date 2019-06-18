package com.evaluation.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TurnRepository extends CrudRepository<Turn, Long> {

	//admin에서 회사별로 turn조회할 때, rest controller에서 사용
	@Query("SELECT t FROM Turn t WHERE t.cno=?1 AND t.tno>0 ORDER BY t.tno ASC")
	public Optional<List<Turn>> getTurnsOfCompany(Long cno);

	// 진행 중인 설문 정보 불러오기 한 회사 안에서 상태와 시작, 종료날짜를 제약 조건으로 한다.
	@Query("SELECT t FROM Turn t WHERE t.cno=?1 AND t.tno>0 AND (t.info360.status='survey' OR t.info360.status='count') AND t.info360.startDate<=?2 AND t.info360.endDate>=?2 ORDER BY t.tno DESC")
	public Optional<List<Turn>> getTurnsInSurvey(Long cno, LocalDateTime threshold);

	// 진행 중인 설문 정보 불러오기 한 회사 안에서 상태와 시작, 종료날짜를 제약 조건으로 한다.
	@Query("SELECT t FROM Turn t WHERE t.cno=?1 AND t.tno>0 AND (t.infoMBO.status='survey' OR t.infoMBO.status='count') AND t.infoMBO.startDate<=?2 AND t.infoMBO.endDate>=?2 ORDER BY t.tno DESC")
	public Optional<List<Turn>> getTurnsInMBO(Long cno, LocalDateTime threshold);

	@Modifying
	@Transactional
	@Query("DELETE FROM Turn t WHERE t.cno=?1")
	public void deleteByCno(long cno);
}
