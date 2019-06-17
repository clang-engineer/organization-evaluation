package com.evaluation.persistence;

import java.util.ArrayList;
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
		Optional<List<Staff>> result = staffRepo.get360Evaluated(10, 9);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});

	}

	@Test
	public void testGetAllStaffListExcludeEvaluator() {
		Optional<List<Staff>> result = staffRepo.get360Evaluator(8, 8, 70);
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
		log.info("" + staffRepo.findByEmail("choij@dwchem.co.kr"));
	}

	@Test
	public void xlWriteTest() {

		staffRepo.findByCno(1L).ifPresent(list -> {
			List<List<String>> xlList = new ArrayList<List<String>>();
			List<String> header = new ArrayList<String>();
			header.add("이름");
			header.add("이메일");
			header.add("부문");
			header.add("부서");
			header.add("직급");
			xlList.add(header);
			for (int i = 0; i < list.size(); i++) {
				List<String> tmpList = new ArrayList<String>();
				tmpList.add(list.get(i).getName());
				tmpList.add(list.get(i).getEmail());
				tmpList.add(list.get(i).getDepartment1());
				tmpList.add(list.get(i).getDepartment2());
				tmpList.add(list.get(i).getLevel());
				xlList.add(tmpList);
			}

			// AboutExcel.writeExcel("test", xlList);
		});

	}

}
