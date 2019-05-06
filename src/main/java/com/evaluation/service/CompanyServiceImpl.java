package com.evaluation.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Company;
import com.evaluation.persistence.CompanyRepository;
import com.evaluation.vo.PageVO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

	@Setter(onMethod_ = @Autowired)
	private CompanyRepository companyRepo;

	@Override
	public void register(Company company) {
		log.info("register..." + company);
		companyRepo.save(company);
	}

	@Override
	public Optional<Company> get(long cno) {
		log.info("get...");
		return companyRepo.findById(cno);
	}

	@Override
	public void modify(Company company) {
		log.info("modify...");
		companyRepo.save(company);
	}

	@Override
	public void remove(long cno) {
		log.info("remove...");
		companyRepo.deleteById(cno);
	}

	@Override
	public Page<Company> getList(PageVO vo) {
		log.info("get List...");
		Pageable page = vo.makePageable(0, "cno");

		Page<Company> result = companyRepo.findAll(companyRepo.makePredicate(vo.getType(), vo.getKeyword()), page);
		return result;
	}

}
