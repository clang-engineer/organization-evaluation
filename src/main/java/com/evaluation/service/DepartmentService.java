package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Department;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface DepartmentService {

	public void register(Department department);

	public Optional<Department> read(long dno);

	public void modify(Department department);

	public void remove(long tno);

	public Page<Department> getListWithPaging(long cno, PageVO vo);
}
