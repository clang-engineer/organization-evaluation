package com.evaluation.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.evaluation.domain.Staff;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

		staff.setCno(1L);
		staffService.register(staff);
	}

	@Test
	public void readTest() {
		log.info("read test...");

		log.info("" + staffService.read(1L));
	}

	@Test
	public void modifyTest() {
		log.info("modify test...");

		Optional<Staff> result = staffService.read(1L);
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

		staffService.remove(2L);
	}

	@Test
	public void testGetDistinctInfoListByCno() {
		log.info("getDistinctInfoListByCno test");

		Map<String, Object> result = staffService.getDistinctInfoListByCno(1L);

		Object lev = result.get("level");
		@SuppressWarnings("unchecked")
		List<String> level = (List<String>) convertObjectToList(lev);
		for (int i = 0; i < level.size(); i++) {
			log.info(level.get(i));
		}

		Object dep = result.get("department");
		@SuppressWarnings("unchecked")
		List<List<String>> department = (List<List<String>>) convertObjectToList(dep);
		department.forEach(data -> {
			log.info(data.get(0));
			log.info(data.get(1));
		});

		Object divObj = result.get("division");
		@SuppressWarnings("unchecked")
		List<List<String>> divList = (List<List<String>>) convertObjectToList(divObj);
		divList.forEach(data -> {
			log.info(data.get(0));
			log.info(data.get(1));
		});
	}

	// cast Object to List
	public static List<?> convertObjectToList(Object obj) {
		List<?> list = new ArrayList<>();
		if (obj.getClass().isArray()) {
			list = Arrays.asList((Object[]) obj);
		} else if (obj instanceof Collection) {
			list = new ArrayList<>((Collection<?>) obj);
		}
		return list;
	}

	@Test
	public void testDeleteDistinctInfoByCno() {
		staffService.deleteDistinctInfoByCno(1L);
	}
}
