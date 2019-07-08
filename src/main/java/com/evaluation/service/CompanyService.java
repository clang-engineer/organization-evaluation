package com.evaluation.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Company;
import com.evaluation.vo.PageVO;

public interface CompanyService {

	void register(Company company);

	Optional<Company> read(long cno);

	void modify(Company company);

	void remove(long cno);

	Page<Company> getList(PageVO page);

	Optional<Company> findByCompanyId(String name);
}
