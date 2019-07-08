package com.evaluation.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.evaluation.domain.Question;
import com.evaluation.vo.PageVO;

public interface QuestionService {
	void register(Question question);

	Optional<Question> read(Long qno);

	void modify(Question question);

	void remove(Long qno);

	Page<Question> getList(long tno, PageVO vo);

	void deleteByTno(long tno);

	Optional<List<List<String>>> getDistinctDivisionCountByTno(long tno);

	Optional<List<List<String>>> getListByDivision(long tno, String division1, String division2);

	Optional<List<Question>> findByTno(long tno);

	Map<String, Object> getDistinctQuestionInfo(long cno, long tno);
}
