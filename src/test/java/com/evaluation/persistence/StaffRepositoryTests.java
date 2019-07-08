package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.RelationSurvey;
import com.evaluation.domain.Staff;

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
public class StaffRepositoryTests {

	@Autowired
	StaffRepository repo;

	@Autowired
	RelationSurveyRepository relationSurveyRepo;

	@Autowired
	RelationMboRepository relationMboRepo;

	@Test
	public void testDI() {
		assertNotNull(repo);
	}

	@Before
	public void insertDummiesTests() {

		IntStream.range(1, 31).forEach(i -> {
			Staff staff = new Staff();
			staff.setEmail("test" + i + "@test.com");
			staff.setPassword("test id" + i);
			staff.setName("name" + i);
			staff.setDepartment1("dep1" + i);
			staff.setDepartment2("dep2" + i);
			staff.setLevel("test id" + i);
			staff.setDivision1("test id" + i);
			staff.setDivision2("test id" + i);
			staff.setCno(1L);
			repo.save(staff);

			RelationSurvey relationSurvey = new RelationSurvey();
			relationSurvey.setEvaluator(staff);
			relationSurvey.setEvaluated(staff);
			relationSurvey.setTno(1L);
			relationSurveyRepo.save(relationSurvey);

			RelationMbo relationMbo = new RelationMbo();
			relationMbo.setEvaluator(staff);
			relationMbo.setEvaluated(staff);
			relationMbo.setTno(1L);
			relationMboRepo.save(relationMbo);

		});
	}

	@Test
	public void testList1() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "name");
		Page<Staff> result = repo.findAll(repo.makePredicate(null, null, 1L), pageable);
		log.info("PAGE : " + result.getPageable());

		result.getContent().forEach(staff -> log.info("" + staff));
	}

	@Test
	public void testList2() {

		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "name");
		Page<Staff> result = repo.findAll(repo.makePredicate("email", "test", 1L), pageable);
		log.info("PAGE : " + result.getPageable());

		result.getContent().forEach(staff -> log.info("" + staff));

	}

	@Test
	public void testGet360EvaluatedList() {
		Optional<List<Staff>> result = repo.get360EvaluatedList(1L, 1L);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});

	}

	@Test
	public void testGet360EvaluatorList() {
		Optional<List<Staff>> result = repo.get360EvaluatorList(1L, 1L, 1L);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});

	}

	@Test
	public void testGetMBOEvaluatedList() {
		Optional<List<Staff>> result = repo.getMboEvaluatedList(1L, 1L);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});

	}

	@Test
	public void testGetMboEvaluatorList() {
		Optional<List<Staff>> result = repo.getMboEvaluatorList(1L, 1L, 1L);
		result.ifPresent(staff -> {
			log.info("===>" + staff);
		});
	}

	@Test
	public void testDeleteByCno() {
		relationSurveyRepo.deleteAll();
		relationMboRepo.deleteAll();
		repo.deleteByCno(1L);
	}

	@Test
	public void testFindByCnoAndName() {
		log.info("" + repo.findByCnoAndName(1, "name1"));
	}

	@Test
	public void testFindByEmail() {
		log.info("" + repo.findByEmail("test@test1.com"));
	}

	@Test
	public void testFindByCno() {
		// xlwritetest
		repo.findByCno(1L).ifPresent(list -> {
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
		});

	}

}
