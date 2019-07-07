package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Division;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface DivisionService {

	public void register(Division division);

	public Optional<Division> read(long dno);

	public void modify(Division division);

	public void remove(long tno);

	public Page<Division> getList(long cno, PageVO vo);
}
