package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationSurvey;
import com.evaluation.domain.Staff;
import com.evaluation.persistence.RelationSurveyRepository;
import com.evaluation.service.RelationSurveyService;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelationSurveyServiceImpl implements RelationSurveyService {

    @Autowired
    RelationSurveyRepository relationSurveyRepo;

    @Override
    public void register(RelationSurvey relationSurvey) {
        log.info("register : " + relationSurvey);

        relationSurveyRepo.save(relationSurvey);
    }

    @Override
    public Optional<RelationSurvey> read(Long rno) {
        log.info("read : " + rno);

        return relationSurveyRepo.findById(rno);
    }

    @Override
    public void modify(RelationSurvey relationSurvey) {
        log.info("modify : " + relationSurvey);

        relationSurveyRepo.findById(relationSurvey.getRno()).ifPresent(origin -> {
            origin.setRno(relationSurvey.getRno());
            origin.setEvaluated(relationSurvey.getEvaluated());
            origin.setEvaluator(relationSurvey.getEvaluator());
            origin.setRelation(relationSurvey.getRelation());
            origin.setTno(relationSurvey.getTno());
            origin.setAnswers(relationSurvey.getAnswers());
            origin.setComments(relationSurvey.getComments());
            origin.setFinish(relationSurvey.getFinish());
            relationSurveyRepo.save(origin);
        });
    }

    @Override
    public void remove(Long rno) {
        log.info("remove : " + rno);

        relationSurveyRepo.deleteById(rno);
    }

    @Override
    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo) {
        log.info("getDistinctEvaluatedList : " + tno + vo);

        Pageable page = vo.makePageable(1, "rno");

        Page<Staff> result = null;
        String type = vo.getType();
        String keyword = vo.getKeyword();

        if (type == null || type.isEmpty()) {
            result = relationSurveyRepo.getDistinctEvaluatedList(tno, page);
        } else if (type.equals("evaluated")) {
            result = relationSurveyRepo.getDistinctEvaluatedListByEvaluated(tno, keyword, page);
        } else if (type.equals("evaluator")) {
            result = relationSurveyRepo.getDistinctEvaluatedListByEvaluator(tno, keyword, page);
        }
        return result;
    }

    @Override
    public Optional<List<RelationSurvey>> findByEvaulated(long tno, long sno) {
        log.info("findByEvaulated " + sno);

        return relationSurveyRepo.findByEvaulated(tno, sno);
    }

    // 회차에 속하는 평가자이면 로그인 true로 하기 위한 서비스
    @Override
    public Optional<Staff> findByEvaluatorEmail(long tno, String email) {
        return relationSurveyRepo.findByEvaluatorEmail(tno, email);
    }

    // 로그인 했을 때 평가할 대상자 뽑기 위한 서비스
    @Override
    public Optional<List<RelationSurvey>> findByEvaluator(long tno, long sno) {
        return relationSurveyRepo.findByEvaulator(tno, sno);
    }

    @Override
    public Optional<List<RelationSurvey>> findAllByTno(long tno) {
        return relationSurveyRepo.findAllByTno(tno);
    }

    @Override
    public List<Staff> findDintinctEavluatedByTno(long tno) {
        return relationSurveyRepo.findDintinctEavluatedByTno(tno);
    }

    @Override
    public Optional<List<List<String>>> progressOfSurevey(long tno) {
        return relationSurveyRepo.progressOfSurevey(tno);
    };
}