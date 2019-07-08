package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Division;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface DivisionService {

	void register(Division division);

	Optional<Division> read(long dno);

	void modify(Division division);

	void remove(long tno);

	Page<Division> getList(long cno, PageVO vo);
}
