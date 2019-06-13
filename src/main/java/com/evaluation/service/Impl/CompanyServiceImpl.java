package com.evaluation.service.Impl;

import java.util.Optional;

import com.evaluation.domain.Company;
import com.evaluation.persistence.CompanyRepository;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.CompanyService;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private CompanyRepository companyRepo;

	private TurnRepository turnRepo;

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

		companyRepo.findById(company.getCno()).ifPresent(origin -> {
			origin.setId(company.getId());
			origin.setName(company.getName());
			origin.setPassword(company.getPassword());
			origin.setHomepage(company.getHomepage());

			origin.setHrManager(company.getHrManager());
			origin.setHrManagerTel(company.getHrManagerTel());

			origin.setUpdateId(company.getUpdateId());
			companyRepo.save(origin);
		});

	}

	// company삭제할 때 turn도 삭제 하기 위함.
	@Override
	public void remove(long cno) {
		log.info("compny+turn delete " + cno);

		companyRepo.deleteById(cno);
		turnRepo.getTurnsOfCompany(cno).ifPresent(turnList -> {
			turnList.forEach(turn -> {
				turnRepo.deleteById(turn.getTno());
			});
		});
	}

	@Override
	public Page<Company> getList(PageVO vo) {
		log.info("service : company getList by" + vo);

		Pageable page = vo.makePageable(0, "cno");
		Page<Company> result = companyRepo.findAll(companyRepo.makePredicate(vo.getType(), vo.getKeyword()), page);
		return result;
	}

	// 회사이름으로 정보 불러오기 위함.
	@Override
	public Optional<Company> readByCompanyId(String name) {
		log.info("compny read by name " + name);
		return companyRepo.findByCompanyId(name);
	}

}
