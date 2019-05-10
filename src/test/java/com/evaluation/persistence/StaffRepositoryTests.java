package com.evaluation.persistence;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.evaluation.domain.Company;
import com.evaluation.domain.Staff;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StaffRepositoryTests {

	@Autowired
	StaffRepository staffRepo;

	@Test
	public void staffTests() {
		log.info("===========================");
		log.info("" + staffRepo);
	}

	@Test
	public void insertDummiesTests() {

		Long[] arr = { 100L, 99L, 98L };

		Arrays.stream(arr).forEach(num -> {

			Company company = new Company();
			company.setCno(num);

			IntStream.range(1, 201).forEach(i -> {
				Staff staff = new Staff();
				staff.setEmail("testemail" + num + i + "@test.com");
				staff.setId("test id" + i);
				staff.setPassword("test id" + i);
				staff.setName("test id" + i);
				staff.setDepartment1("test id" + i);
				staff.setDepartment2("department2");
				staff.setLevel("test id" + i);
				staff.setDivision1("test id" + i);
				staff.setDivision2("test id" + i);
				staff.setCompany(company);

				staffRepo.save(staff);
			});
		});
	}

	@Test
	public void testList1() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "name");
		Page<Staff> result = staffRepo.findAll(staffRepo.makePredicate(null, null, 100L), pageable);
		log.info("PAGE : " + result.getPageable());

		log.info("----------------");
		result.getContent().forEach(staff -> log.info("" + staff));
	}

	@Test
	public void testList2() {

		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "name");
		Page<Staff> result = staffRepo.findAll(staffRepo.makePredicate("email", "02", 100L), pageable);
		log.info("PAGE : " + result.getPageable());

		log.info("----------------");
		result.getContent().forEach(staff -> log.info("" + staff));

	}

}
