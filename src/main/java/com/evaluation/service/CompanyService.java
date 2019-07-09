package com.evaluation.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Company;
import com.evaluation.vo.PageVO;

/**
 * <code>CompanyService</code> 객체는 Company객체 sevice 계층의 interface를 표현한다.
 */
public interface CompanyService {

	/**
	 * 회사를 등록한다.
	 * 
	 * @param company 회사 객체
	 */
	void register(Company company);

	/**
	 * 회사를 읽어온다.
	 * 
	 * @param cno 회사 id
	 * @return 회사 객체
	 */
	Optional<Company> read(long cno);

	/**
	 * 회사를 수정한다.
	 * 
	 * @param company 회사 객체
	 */
	void modify(Company company);

	/**
	 * 회사를 삭제한다.
	 * 
	 * @param cno 회사 id
	 */
	void remove(long cno);

	/**
	 * 회사 리스트를 출력한다.
	 * 
	 * @param page 페이지 정보
	 * @return 회사 리스트
	 */
	Page<Company> getList(PageVO page);

	/**
	 * 회사 아이디로 회사를 찾는다.
	 * 
	 * @param id 회사의 고유 id
	 * @return 회사 객체
	 */
	Optional<Company> findByCompanyId(String id);
}
