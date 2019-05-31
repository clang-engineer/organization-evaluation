package com.evaluation.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	public void testGetAllList() {
		List<Staff> result = staffRepo.getAllStaffListByCno(10);
		log.info("===>>" + result);
		result.forEach(staff -> {
			log.info("===>" + staff.getName());
		});
	}

	@Test
	public void testGetAllStaffListExcludeEvaluated() {
		List<Staff> result = staffRepo.getStaffForEvaluated(10, 9);
		result.forEach(staff -> {
			log.info("===>" + staff.getSno());
		});

	}

	@Test
	public void testGetAllStaffListExcludeEvaluator() {
		List<Staff> result = staffRepo.getStaffForEvaluator(8, 8, 70);
		result.forEach(staff -> {
			log.info("===>" + staff.getSno());
		});

	}

	@Test
	public void testDeleteAll() {
		staffRepo.deleteByCno(1L);
	}

	@Test
	public void testGetDistinctDepartmentListByCno() {
		List<List<String>> result = new ArrayList<List<String>>();
		staffRepo.getDistinctDepartmentListByCno(1L).forEach(arr -> result.add(arr));

		result.forEach(data -> log.info("" + data.get(1)));
		log.info("" + result);
	}

	@Test
	public void testGetDistinctDivisionListByCno() {
		List<List<String>> result = new ArrayList<List<String>>();
		staffRepo.getDistinctDivisionListByCno(1L).forEach(arr -> result.add(arr));

		result.forEach(data -> log.info("" + data.get(1)));
		log.info("" + result);
	}

	@Test
	public void testGetDistinctLevelListByCno() {
		List<String> result = new ArrayList<String>();
		staffRepo.getDistinctLevelListByCno(1L).forEach(arr -> result.add(arr));

		result.forEach(data -> log.info("" + data));
		log.info("" + result);
	}
}
