package com.evaluation.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Staff;
import com.evaluation.persistence.StaffRepository;
import com.evaluation.service.StaffService;
import com.evaluation.vo.PageVO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

	@Setter(onMethod_ = { @Autowired })
	StaffRepository staffRepo;

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

		Pageable page = vo.makePageable(0, "sno");
		Page<Staff> result = staffRepo.findAll(staffRepo.makePredicate(vo.getType(), vo.getKeyword(), cno), page);
		return result;
	}

}
