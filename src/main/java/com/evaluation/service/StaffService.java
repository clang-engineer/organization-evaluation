package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface StaffService {
	public void register(Staff staff);

	public Optional<Staff> read(Long sno);

	public void modify(Staff staff);

	public void remove(Long sno);

	public Page<Staff> getList(long cno, PageVO vo);

	public Optional<List<Staff>> getEvaluatedList(long cno, long tno);

	public Optional<List<Staff>> getEvaluatorList(long cno, long tno, long sno);

	public void deleteByCno(long cno);

	public void deleteDistinctInfoByCno(long cno);

	public Optional<Staff> readByEmail(String email);

	public Optional<Staff> readByCnoAndName(long cno, String name);
}
