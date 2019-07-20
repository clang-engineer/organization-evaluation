package com.evaluation.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Question;
import com.evaluation.persistence.DivisionRepository;
import com.evaluation.persistence.QuestionRepository;
import com.evaluation.service.QuestionService;
import com.evaluation.vo.PageVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	QuestionRepository questionRepo;

	@Autowired
	DivisionRepository divisionRepo;

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

		questionRepo.findById(question.getQno()).ifPresent(origin -> {
			origin.setCategory(question.getCategory());
			origin.setIdx(question.getIdx());
			origin.setItem(question.getItem());
			origin.setDivision1(question.getDivision1());
			origin.setDivision2(question.getDivision2());
			origin.setRatio(question.getRatio());
			origin.setUpdateId(question.getUpdateId());
			questionRepo.save(origin);
		});
	}

	@Override
	public void remove(Long qno) {
		log.info("delete " + qno);
		questionRepo.findById(qno).ifPresent(origin -> {
			questionRepo.deleteById(origin.getQno());
		});
	}

	@Override
	public Page<Question> getList(long tno, PageVO vo) {
		log.info("getList " + tno + vo);

		Pageable page = vo.makePageable(1, "qno");
		Page<Question> result = questionRepo.findAll(questionRepo.makePredicate(vo.getType(), vo.getKeyword(), tno),
				page);
		return result;
	}

	@Override
	public void deleteByTno(long tno) {
		log.info("delete by tno : " + tno);

		questionRepo.deleteByTno(tno);
	}

	@Override
	public Optional<List<List<String>>> getDistinctDivisionCountByTno(long tno) {
		log.info("getDistinctDivision by tno : " + tno);

		return questionRepo.getDistinctDivisionCountByTno(tno);
	}

	@Override
	public Optional<List<Question>> getListByDivision(long tno, String division1, String division2) {
		log.info("getDivision by tno : " + tno);

		return questionRepo.getListByDivision(tno, division1, division2);
	}

	public Optional<List<Question>> findByTno(long tno) {
		return questionRepo.findByTno(tno);
	}

	@Override
	// 질문 등록할 때 중복제거한 리스트 전달하기 위한 서비스
	public Map<String, Object> getDistinctQuestionInfo(long cno, long tno) {

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("division1", divisionRepo.getListDivision1(cno));
		result.put("division2", divisionRepo.getListDivision2(cno));
		result.put("category", questionRepo.getListCategory(tno));

		return result;
	}
}
