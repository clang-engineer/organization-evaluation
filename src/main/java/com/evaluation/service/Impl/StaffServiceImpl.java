package com.evaluation.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.evaluation.domain.Staff;
import com.evaluation.persistence.DepartmentRepository;
import com.evaluation.persistence.DivisionRepository;
import com.evaluation.persistence.LevelRepository;
import com.evaluation.persistence.StaffRepository;
import com.evaluation.service.StaffService;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

	StaffRepository staffRepo;

	DepartmentRepository departmentRepo;

	DivisionRepository divisionRepo;

	LevelRepository levelRepo;

	@Override
	public void register(Staff staff) {
		log.info("service : staff register " + staff);

		staffRepo.save(staff);
	}

	@Override
	public Optional<Staff> read(Long sno) {
		log.info("service : staff read " + sno);

		return staffRepo.findById(sno);
	}

	@Override
	public void modify(Staff staff) {
		log.info("service : staff modify " + staff);

		staffRepo.findById(staff.getSno()).ifPresent(origin -> {
			origin.setCno(staff.getCno());
			origin.setEmail(staff.getEmail());
			origin.setName(staff.getName());
			origin.setPassword(staff.getPassword());
			origin.setDepartment1(staff.getDepartment1());
			origin.setDepartment2(staff.getDepartment2());
			origin.setLevel(staff.getLevel());
			origin.setDivision1(staff.getDivision1());
			origin.setDivision2(staff.getDivision2());
			origin.setUpdateId(staff.getUpdateId());
			origin.setTelephone(staff.getTelephone());
			staffRepo.save(origin);
		});
	}

	@Override
	public void remove(Long sno) {
		log.info("service : staff remove " + sno);

		staffRepo.deleteById(sno);
	}

	@Override
	public Page<Staff> getList(long cno, PageVO vo) {
		log.info("service : staff getList " + cno + vo);

		Pageable page = vo.makePageable(1, "sno");
		Page<Staff> result = staffRepo.findAll(staffRepo.makePredicate(vo.getType(), vo.getKeyword(), cno), page);
		return result;
	}

	@Override
	public Optional<List<Staff>> get360EvaluatedList(long cno, long tno) {
		log.info("get EvaluatedList list by " + cno);

		Optional<List<Staff>> result = staffRepo.get360Evaluated(cno, tno);
		return result;
	}

	@Override
	public Optional<List<Staff>> get360EvaluatorList(long cno, long tno, long sno) {
		log.info("get EvaluatedList list by " + cno);

		Optional<List<Staff>> result = staffRepo.get360Evaluator(cno, tno, sno);
		return result;
	}

	@Override
	public Optional<List<Staff>> getMboEvaluatedList(long cno, long tno) {
		log.info("get EvaluatedList list by " + cno);

		Optional<List<Staff>> result = staffRepo.getMboEvaluated(cno, tno);
		return result;
	}

	@Override
	public Optional<List<Staff>> getMboEvaluatorList(long cno, long tno, long sno) {
		log.info("get EvaluatedList list by " + cno);

		Optional<List<Staff>> result = staffRepo.getMboEvaluator(cno, tno, sno);
		return result;
	}

	@Override
	public void deleteByCno(long cno) {
		log.info("delete by cno : " + cno);

		staffRepo.deleteByCno(cno);
	}

	@Override
	public void deleteDistinctInfoByTnoCno(long tno, long cno) {
		log.info("deleteDistinctInfoByTnoCno " + tno);

		departmentRepo.deleteByTno(tno);

		levelRepo.deleteByCno(cno);
		divisionRepo.deleteByCno(cno);
	}

	@Override
	public Optional<Staff> readByEmail(String email) {
		return staffRepo.findByEmail(email);
	}

	@Override
	public Optional<Staff> readByCnoAndName(long cno, String name) {
		return staffRepo.findByCnoAndName(cno, name);
	}

	@Override
	public Optional<List<Staff>> readBycno(long cno) {
		return staffRepo.findByCno(cno);
	}

	@Override
	public Map<String, Object> getDistinctInfo(long cno, long tno) {

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("department1", departmentRepo.getListDepartment1(tno));
		result.put("department2", departmentRepo.getListDepartment2(tno));
		result.put("division1", divisionRepo.getListDivision1(cno));
		result.put("division2", divisionRepo.getListDivision2(cno));
		result.put("level", levelRepo.getListLevel(cno));

		return result;
	}

}
