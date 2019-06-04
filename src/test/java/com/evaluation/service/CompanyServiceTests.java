package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Company;
import com.evaluation.vo.PageVO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Commit
public class CompanyServiceTests {

	@Setter(onMethod_ = { @Autowired })
	private CompanyService service;

	@Test
	public void testExist() {
		log.info("========== test exist service");
		log.info("" + service);
		assertNotNull(service);
	}

	@Test
	public void register() {
		log.info("========== test register");
		Company company = new Company();

		company.setId("test id 1");
		company.setName("test name 1");

		service.register(company);

	}

	@Test
	public void get() {
		log.info("========== test get");
		log.info("" + service.get(1L));
	}

	@Test
	public void testModify() {
		log.info("========== test Modify");
		Optional<Company> co = service.get(100L);
		Company company = co.get();
		company.setId("test modify id 303");
		company.setName("test modify name 303");
		service.modify(company);
	}

	@Test
	public void testDelete() {
		log.info("========== test delete");
		service.remove(1L);
	}

	@Test
	public void getList() {
		log.info("========== test getList");
		PageVO pageVO = new PageVO();
		service.getList(pageVO).forEach(company -> log.info("" + company));
	}

	@Test
	public void testReadByName() {
		log.info("" + service.readByCompanyId("siliconmitus"));
	}
}
