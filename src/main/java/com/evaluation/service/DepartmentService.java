package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Department;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>DepartmentService</code> 객체는 Department객체 sevice 계층의 interface를 표현한다.
 */
public interface DepartmentService {

	/**
	 * 부서를 등록한다.
	 * 
	 * @param department 부서 객체
	 */
	void register(Department department);

	/**
	 * 부서를 읽어온다.
	 * 
	 * @param dno 부서 id
	 * @return 부서 객체
	 */
	Optional<Department> read(long dno);

	/**
	 * 부서를 수정한다.
	 * 
	 * @param department 부서 객체
	 */
	void modify(Department department);

	/**
	 * 부서를 삭제한다.
	 * 
	 * @param dno 부서 id
	 */
	void remove(long dno);

	/**
	 * 회차의 부서 정보를 반환한다.
	 * 
	 * @param tno 회차 id
	 * @param vo  페이지 정보
	 * @return 부서 정보
	 */
	Page<Department> getList(long tno, PageVO vo);

	/**
	 * 회차의 직원의 부서 정보를 불러온다.
	 * 
	 * @param tno 회차 id
	 * @param sno 직원 id
	 * @return 부서 정보 리스트
	 */
	Optional<List<Department>> findByTnoSno(long tno, long sno);

	/**
	 * 회차의 부서 정보를 불러온다.
	 * 
	 * @param tno         회차 id
	 * @param department1 부문명1
	 * @param department2 부서명1
	 * @return 부서객체
	 */
	Optional<Department> findByDepartment(long tno, String department1, String department2);
}
