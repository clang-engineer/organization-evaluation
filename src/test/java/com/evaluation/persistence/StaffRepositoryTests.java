package com.evaluation.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.evaluation.domain.Staff;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

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

		Long[] arr = { 10L, 9L, 8L };

		Arrays.stream(arr).forEach(num -> {

			IntStream.range(1, 31).forEach(i -> {
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
				staff.setCno(num);

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

	@Test
	public void testGetAllStaffListExcludeEvaluated() {
		Optional<List<Staff>> result = staffRepo.getStaffForEvaluated(10, 9);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});

	}

	@Test
	public void testGetAllStaffListExcludeEvaluator() {
		Optional<List<Staff>> result = staffRepo.getStaffForEvaluator(8, 8, 70);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});

	}

	@Test
	public void testDeleteAll() {
		staffRepo.deleteByCno(1L);
	}

	@Test
	public void testFindByCnoAndName() {
		log.info("" + staffRepo.findByCnoAndName(1, "이동영"));
	}

	@Test
	public void testFindByCnoAndEmail() {
		log.info("" + staffRepo.findByCnoAndEmail(1L, "choij@dwchem.co.kr"));
	}

	// relation 설정할 때 직원 불러오기 위해! evaluator 위해
	// public Staff findByCnoEqualAndNameEqual(long cno, String email);

}
