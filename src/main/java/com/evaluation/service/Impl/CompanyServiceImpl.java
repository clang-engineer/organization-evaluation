package com.evaluation.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Company;
import com.evaluation.persistence.CompanyRepository;
import com.evaluation.service.CompanyService;
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
		log.info("service : company register " + company);
		
		companyRepo.save(company);
	}

	@Override
	public Optional<Company> get(long cno) {
		log.info("service : company get " + cno);
		
		return companyRepo.findById(cno);
	}

	@Override
	public void modify(Company company) {
		log.info("service : company modify " + company);
		
		companyRepo.save(company);
	}

	@Override
	public void remove(long cno) {
		log.info("service : company remove " + cno);
		
		companyRepo.deleteById(cno);
	}

	@Override
	public Page<Company> getList(PageVO vo) {
		log.info("service : company getList by" + vo);

		Pageable page = vo.makePageable(0, "cno");
		Page<Company> result = companyRepo.findAll(companyRepo.makePredicate(vo.getType(), vo.getKeyword()), page);
		return result;
	}

}
