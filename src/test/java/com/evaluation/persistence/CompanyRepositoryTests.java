package com.evaluation.persistence;

import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Company;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Commit
public class CompanyRepositoryTests {

	@Autowired
	CompanyRepository repo;

	@Test
	public void insertCompanyDummies() {

		IntStream.range(1, 101).forEach(i -> {

			Company company = new Company();
			company.setId("sample id " + i);
			company.setName("sample name " + i);
			company.setPassword("password " + i);
			repo.save(company);
		});

	}

	@Test
	public void testList1() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "cno");
		Page<Company> result = repo.findAll(repo.makePredicate(null, null), pageable);
		log.info("PAGE : " + result.getPageable());

		log.info("----------------");
		result.getContent().forEach(company -> log.info("" + company));
	}

	@Test
	public void testList2() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "cno");
		Page<Company> result = repo.findAll(repo.makePredicate("id", "1"), pageable);
		log.info("PAGE : " + result.getPageable());

		log.info("----------------");
		result.getContent().forEach(company -> log.info("" + company));
	}

	@Test
	public void testFindByComId() {
		log.info("" + repo.findByCompanyId("siliconmitus"));
	}

}
