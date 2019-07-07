package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;
import com.evaluation.persistence.RelationMboRepository;
import com.evaluation.service.RelationMboService;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelationMboServiceImpl implements RelationMboService {

    @Autowired
    RelationMboRepository relationMboRepo;

    @Override
    public void register(RelationMbo relationMbo) {
        log.info("register : " + relationMbo);

        relationMboRepo.save(relationMbo);
    }

    @Override
    public Optional<RelationMbo> read(Long rno) {
        log.info("read : " + rno);

        return relationMboRepo.findById(rno);
    }

    @Override
    public void modify(RelationMbo relationMbo) {
        log.info("modify : " + relationMbo);

        relationMboRepo.findById(relationMbo.getRno()).ifPresent(origin -> {
            origin.setRno(relationMbo.getRno());
            origin.setEvaluated(relationMbo.getEvaluated());
            origin.setEvaluator(relationMbo.getEvaluator());
            origin.setRelation(relationMbo.getRelation());
            origin.setTno(relationMbo.getTno());
            origin.setAnswers(relationMbo.getAnswers());
            origin.setComments(relationMbo.getComments());
            origin.setFinish(relationMbo.getFinish());
            relationMboRepo.save(origin);
        });
    }

    @Override
    public void remove(Long rno) {
        log.info("remove : " + rno);

        relationMboRepo.deleteById(rno);
    }

    @Override
    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo) {
        log.info("getDistinctEvaluatedList : " + tno + vo);

        Pageable page = vo.makePageable(1, "rno");

        Page<Staff> result = null;
        String type = vo.getType();
        String keyword = vo.getKeyword();

        if (type == null || type.isEmpty()) {
            result = relationMboRepo.getDistinctEvaluatedList(tno, page);
        } else if (type.equals("evaluated")) {
            result = relationMboRepo.getDistinctEvaluatedListByEvaluated(tno, keyword, page);
        } else if (type.equals("evaluator")) {
            result = relationMboRepo.getDistinctEvaluatedListByEvaluator(tno, keyword, page);
        }
        return result;
    }

    @Override
    public Optional<List<RelationMbo>> findByEvaulated(long tno, long sno) {
        log.info("findByEvaulated " + sno);

        return relationMboRepo.findByEvaulated(tno, sno);
    }

    // 회차에 속하는 평가자이면 로그인 true로 하기 위한 서비스
    @Override
    public Optional<Staff> findByEvaluatorEmail(long tno, String email) {
        return relationMboRepo.findByEvaluatorEmail(tno, email);
    }

    // 로그인 했을 때 평가할 대상자 뽑기 위한 서비스
    @Override
    public Optional<List<RelationMbo>> findByEvaluator(long tno, long sno) {
        return relationMboRepo.findByEvaulator(tno, sno);
    }

    @Override
    public Optional<List<RelationMbo>> findAllByTno(long tno) {
        return relationMboRepo.findAllByTno(tno);
    }

    @Override
    public List<Staff> findDintinctEavluatedByTno(long tno) {
        return relationMboRepo.findDintinctEavluatedByTno(tno);
    }

    @Override
    public Optional<List<List<String>>> progressOfSurevey(long tno) {
        return relationMboRepo.progressOfSurevey(tno);
    };

    @Override
    public Optional<RelationMbo> findMeRelationByTnoSno(long tno, long sno) {

        return relationMboRepo.findMeRelationByTnoSno(tno, sno);
    }

    @Override
    public Optional<List<List<String>>> progressOfPlan(long tno) {
        return relationMboRepo.progressOfPlan(tno);
    }
}