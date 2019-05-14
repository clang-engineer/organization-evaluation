package com.evaluation.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Question;
import com.evaluation.persistence.QuestionRepository;
import com.evaluation.service.QuestionService;
import com.evaluation.vo.PageVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	QuestionRepository questionRepo;

	@Override
	public void register(Question question) {
		log.info("register " + question);
		questionRepo.save(question);
	}

	@Override
	public Optional<Question> read(Long qno) {
		log.info("read " + qno);
		return questionRepo.findById(qno);
	}

	@Override
	public void modify(Question question) {
		log.info("modify " + question);
		questionRepo.save(question);
	}

	@Override
	public void remove(Long qno) {
		log.info("delete " + qno);
		questionRepo.deleteById(qno);
	}

	@Override
	public Page<Question> getList(long tno, PageVO vo) {
		log.info("getList " + tno + vo);

		Pageable page = vo.makePageable(0, "sno");
		Page<Question> result = questionRepo.findAll(questionRepo.makePredicate(vo.getType(), vo.getKeyword(), tno), page);
		return result;
	}

}
