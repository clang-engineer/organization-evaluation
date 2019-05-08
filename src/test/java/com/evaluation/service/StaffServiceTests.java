package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Company;
import com.evaluation.domain.Staff;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StaffServiceTests {

	@Setter(onMethod_ = { @Autowired })
	StaffService staffService;

	@Test
	public void exitsTest() {
		assertNotNull(staffService);
	}

	@Test
	public void registerTest() {
		log.info("register test...");

		Staff staff = new Staff();
		staff.setEmail("test2@test.com");
		staff.setId("service test id");
		staff.setPassword("service test pwd");
		staff.setName("service test name");
		staff.setDepartment1("department1");
		staff.setDepartment2("department2");
		staff.setLevel("level");
		staff.setDivision1("division1");
		staff.setDivision2("division2");

		Company company = new Company();
		company.setCno(1L);

		staff.setCompany(company);
		staffService.register(staff);
	}

	@Test
	public void readTest() {
		log.info("read test...");

		log.info("" + staffService.read("test@test.com"));
	}

	@Test
	public void modifyTest() {
		log.info("modify test...");

		Optional<Staff> result = staffService.read("test@test.com");
		Staff staff = result.get();
		staff.setId("service test modify id");
		staff.setPassword("service test modify pwd");
		staff.setName("service test modify name");
		staff.setDepartment1("modify department1");
		staff.setDepartment2("modify department2");
		staff.setLevel("modify level");
		staff.setDivision1("modify division1");
		staff.setDivision2("modify division2");

		staffService.modify(staff);
	}
	
	@Test
	public void removeTest() {
		log.info("remove test...");
		
		staffService.remove("test@test.com");
	}
}
