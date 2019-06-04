package com.evaluation.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Company;
import com.evaluation.vo.PageVO;

public interface CompanyService {

	public void register(Company company);

	public Optional<Company> get(long cno);

	public void modify(Company company);

	public void remove(long cno);

	public Page<Company> getList(PageVO page);

	public Company readByCompanyId(String name);
}
