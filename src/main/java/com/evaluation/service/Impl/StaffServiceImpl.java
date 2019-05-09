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
	public Optional<Staff> read(String email) {
		log.info("service : staff read " + email);

		return staffRepo.findById(email);
	}

	@Override
	public void modify(Staff staff) {
		log.info("service : staff modify " + staff);

		staffRepo.save(staff);
	}

	@Override
	public void remove(String email) {
		log.info("service : staff remove " + email);

		staffRepo.deleteById(email);
	}

	@Override
	public Page<Staff> getList(long cno, PageVO vo) {
		log.info("service : staff getList " + cno + vo);

		Pageable page = vo.makePageable(1, "name");
		Page<Staff> result = staffRepo.findAll(staffRepo.makePredicate(vo.getType(), vo.getKeyword(), cno), page);
		return result;
	}

}
