package com.evaluation.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

public interface StaffService {
	public void register(Staff staff);

	public Optional<Staff> read(String email);

	public void modify(Staff staff);

	public void remove(String email);

	public Page<Staff> readList(long cno, PageVO vo);
}
