package com.evaluation.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <code>TurnRepository</code> 객체는 Turn 객체의 영속화를 위해 표현한다.
 */
public interface TurnRepository extends CrudRepository<Turn, Long> {

	/**
	 * 한 회사에 속하는 회차 리스트를 찾는다..
	 * 
	 * @param cno 회사id
	 * @return 회차 객체 리스트
	 */
	@Query("SELECT t FROM Turn t WHERE t.cno=?1 AND t.tno>0 ORDER BY t.tno ASC")
	public Optional<List<Turn>> getTurnsOfCompany(Long cno);

	/**
	 * 서베이 상태가 설정, 비활성화가 아니고 시작날짜와 종료날짜 사이에 현재날짜가 존재하는 회차 정보를 찾는다.
	 * 
	 * @param cno       회사id
	 * @param threshold 현재 시간
	 * @return 회차 객체 리스트
	 */
	@Query("SELECT t FROM Turn t WHERE t.cno=:cno AND t.tno>0 AND t.infoSurvey.status NOT IN ('setting', 'inactivation')  AND :time BETWEEN t.infoSurvey.startDate AND t.infoSurvey.endDate ORDER BY t.tno DESC")
	public Optional<List<Turn>> getTurnsInSurvey(@Param("cno") Long cno, @Param("time") LocalDateTime threshold);

	/**
	 * Mb 상태가 설정, 비활성화가 아니고 시작날짜와 종료날짜 사이에 현재날짜가 존재하는 회차 정보를 찾는다.
	 * 
	 * @param cno       회사id
	 * @param threshold 현재 시간
	 * @return 회차 객체 리스트
	 */
	@Query("SELECT t FROM Turn t WHERE t.cno=:cno AND t.tno>0 AND t.infoMbo.status NOT IN ('setting', 'inactivation') AND :time BETWEEN t.infoMbo.startDate AND t.infoMbo.endDate ORDER BY t.tno DESC")
	public Optional<List<Turn>> getTurnsInMbo(@Param("cno") Long cno, @Param("time") LocalDateTime threshold);

	/**
	 * 한 회사에 속하는 회차를 모두 삭제한다.
	 * 
	 * @param cno 회사id
	 */
	@Modifying
	@Transactional
	@Query("DELETE FROM Turn t WHERE t.cno=?1")
	public void deleteByCno(long cno);
}
