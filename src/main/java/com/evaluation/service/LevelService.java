package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Level;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface LevelService {

	void register(Level level);

	Optional<Level> read(long lno);

	void modify(Level level);

	void remove(long lno);

	Page<Level> getList(long cno, PageVO vo);
}
