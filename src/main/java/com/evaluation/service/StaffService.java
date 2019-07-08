package com.evaluation.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface StaffService {
	void register(Staff staff);

	Optional<Staff> read(Long sno);

	void modify(Staff staff);

	void remove(Long sno);

	Page<Staff> getList(long cno, PageVO vo);

	Optional<List<Staff>> get360EvaluatedList(long cno, long tno);

	Optional<List<Staff>> get360EvaluatorList(long cno, long tno, long sno);

	Optional<List<Staff>> getMboEvaluatedList(long cno, long tno);

	Optional<List<Staff>> getMboEvaluatorList(long cno, long tno, long sno);

	void deleteByCno(long cno);

	void deleteDistinctInfoByTnoCno(long tno, long cno);

	Optional<Staff> findByEmail(String email);

	Optional<Staff> findByCnoAndName(long cno, String name);

	Optional<List<Staff>> findByCno(long cno);

	Map<String, Object> getDistinctInfo(long cno, long tno);
}
