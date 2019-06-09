package com.evaluation.service.Impl;

import java.util.List;
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
			origin.setEmail(staff.getEmail());
			origin.setName(staff.getName());
			origin.setId(staff.getId());
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
	public Optional<List<Staff>> getEvaluatedList(long cno, long tno) {
		log.info("get EvaluatedList list by " + cno);

		Optional<List<Staff>> result = staffRepo.getStaffForEvaluated(cno, tno);
		return result;
	}

	@Override
	public Optional<List<Staff>> getEvaluatorList(long cno, long tno, long sno) {
		log.info("get EvaluatedList list by " + cno);

		Optional<List<Staff>> result = staffRepo.getStaffForEvaluator(cno, tno, sno);
		return result;
	}

	@Override
	public void deleteByCno(long cno) {
		log.info("delete by tno : " + cno);

		staffRepo.deleteByCno(cno);
	}

	@Override
	public void deleteDistinctInfoByCno(long cno) {
		log.info("deleteDistinctInfoByCno " + cno);

		departmentRepo.deleteByCno(cno);
		levelRepo.deleteByCno(cno);
		divisionRepo.deleteByCno(cno);
	}

	@Override
	public Staff readByCnoAndEmail(long cno, String email) {
		return staffRepo.findByCnoAndEmail(cno, email);
	}

	@Override
	public Staff readByCnoAndName(long cno, String name) {
		return staffRepo.findByCnoAndName(cno, name);
	}
}
