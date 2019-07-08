package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import com.evaluation.domain.Company;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class CompanyRepositoryTests {

	@Autowired
	CompanyRepository repo;

	@Test
	public void testtDI() {
		assertNotNull(repo);
	}

	@Before
	public void setBefore() {
		IntStream.range(1, 4).forEach(i -> {
			Company company = new Company();
			company.setId("sampleid" + i);
			company.setName("sample name " + i);
			company.setPassword("password " + i);
			repo.save(company);
		});
	}

	@Test
	public void testRead() {

		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "cno");
		Page<Company> result = repo.findAll(repo.makePredicate(null, null), pageable);
		log.info("PAGE : " + result.getPageable());

		result.getContent().forEach(company -> log.info("" + company));
	}

	@Test
	public void testRead2() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "cno");
		Page<Company> result = repo.findAll(repo.makePredicate("id", "1"), pageable);
		log.info("PAGE : " + result.getPageable());

		result.getContent().forEach(company -> log.info("" + company));
	}

	@Test
	public void testFindByComId() {

		repo.findByCompanyId("sampleid2");
	}

	@Test
	public void testUpdate() {
		repo.findByCompanyId("sampleid2").ifPresent(company -> {
			repo.findById(company.getCno()).ifPresent(origin -> {
				origin.setName("test..");
				repo.save(origin);
			});
		});
	}

	@Test
	public void testDelete() {
		repo.findByCompanyId("sampleid2").ifPresent(company -> {
			repo.deleteById(company.getCno());
		});

	}
}
