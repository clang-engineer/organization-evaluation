package com.evaluation.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>StaffService</code> 객체는 Staff객체 sevice 계층의 interface를 표현한다.
 */
public interface StaffService {
	/**
	 * 직원 정볼들 등록한다.
	 * 
	 * @param staff 직원정보
	 */
	void register(Staff staff);

	/**
	 * 직원 정보를 찾는다.
	 * 
	 * @param sno 직원 id
	 * @return 직원 정보
	 */
	Optional<Staff> read(Long sno);

	/**
	 * 직원 정볼들 수정한다.
	 * 
	 * @param staff 직원정보
	 */
	void modify(Staff staff);

	/**
	 * 직원 정보를 삭제한다..
	 * 
	 * @param sno 직원 id
	 */
	void remove(Long sno);

	/**
	 * 한 회사의 모든 직원 정보를 찾는다.
	 * 
	 * @param cno 회사 id
	 * @param vo  페이지 정보
	 * @return 페이징 처리된 직원 정보 리스트
	 */
	Page<Staff> getList(long cno, PageVO vo);

	/**
	 * 한 회사에서 한 회차에 속하는 본인 평가 관계정보에 없는 직원정보를 찾는다. (설정 시 펴핑가자 출력하기 위해)
	 * 
	 * @param cno 회사 id
	 * @param tno 회차 id
	 * @return 직원 객체 리스트
	 */
	Optional<List<Staff>> get360EvaluatedList(long cno, long tno);

	/**
	 * 전 직원 중에 직원 정보가 동일하지 않고(본인이 아닌), 평가자에 속하지 않은 직원정보를 찾는다. (설정 시 평가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @param sno 직원id
	 * @return 직원 객체 리스트
	 */
	Optional<List<Staff>> get360EvaluatorList(long cno, long tno, long sno);

	/**
	 * 한 회사에서 한 회차에 속하는 본인 평가 관계정보에 없는 직원정보를 찾는다. (설정 시 펴핑가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @return 직원 객체 리스트
	 */
	Optional<List<Staff>> getMboEvaluatedList(long cno, long tno);

	/**
	 * 전 직원 중에 직원 정보가 동일하지 않고(본인이 아닌), 평가자에 속하지 않은 직원정보를 찾는다. (설정 시 평가자 출력하기 위해)
	 * 
	 * @param cno 회사id
	 * @param tno 회차id
	 * @param sno 직원id
	 * @return 직원 객체 리스트
	 */
	Optional<List<Staff>> getMboEvaluatorList(long cno, long tno, long sno);

	/**
	 * 한 회사에 속하는 모든 직원정보를 삭제한다.
	 * 
	 * @param cno 회사id
	 */
	void deleteByCno(long cno);

	/**
	 * 한 회사 회차의 모든 부문부서, 직군계층, 직급정보를 삭제한다.
	 * 
	 * @param cno 회사 id
	 * @param tno 회차 id
	 */
	void deleteDistinctInfoByTnoCno(long cno, long tno);

	/**
	 * 이메일로 직원 정보를 찾는다. (엑셀 피평가자)
	 * 
	 * @param email 이메일
	 * @return 직원 객체 리스트
	 */
	Optional<Staff> findByEmail(String email);

	/**
	 * 이름으로 직원 정보를 찾는다. (엑셀 평가자)
	 * 
	 * @param cno  회사id
	 * @param name 이름
	 * @return 직원 객체
	 */
	Optional<Staff> findByCnoAndName(long cno, String name);

	/**
	 * 한 회사에 속하는 모든 직원 정보를 찾는다.
	 * 
	 * @param cno 회사id
	 * @return 직원 객체 리스트
	 */
	Optional<List<Staff>> findByCno(long cno);

	/**
	 * 한 회사 회차의 모든 부문, 부서, 직군, 계층, 직급 중복 제거한 정보 얻는다.
	 * 
	 * @param cno 회사 id
	 * @param tno 회차 id
	 * @return 중복제거 한 정보리스트
	 */
	Map<String, Object> getDistinctInfo(long cno, long tno);
}
