package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Level;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface LevelService {

	public void register(Level level);

	public Optional<Level> read(long lno);

	public void modify(Level level);

	public void remove(long lno);

	public Page<Level> getList(long cno, PageVO vo);
}
