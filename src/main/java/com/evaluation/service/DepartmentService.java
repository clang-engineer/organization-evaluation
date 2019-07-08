package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Department;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface DepartmentService {

	void register(Department department);

	Optional<Department> read(long dno);

	void modify(Department department);

	void remove(long tno);

	Page<Department> getList(long tno, PageVO vo);

	Optional<List<Department>> findByTnoSno(long tno, long sno);

	Optional<Department> findByDepartment(long tno, String department1, String department2);
}
