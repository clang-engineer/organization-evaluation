package com.evaluation.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Question;
import com.evaluation.vo.PageVO;

public interface QuestionService {
	public void register(Question question);

	public Optional<Question> read(Long qno);

	public void modify(Question question);

	public void remove(Long qno);

	public Page<Question> getList(long tno, PageVO vo);
}
