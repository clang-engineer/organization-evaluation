package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.RelationMBO;
import com.evaluation.domain.Staff;
import com.evaluation.persistence.RelationMBORepository;
import com.evaluation.service.RelationMBOService;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelationMBOServiceImpl implements RelationMBOService {

    @Setter(onMethod_ = { @Autowired })
    RelationMBORepository relationMBORepo;

    @Override
    public void register(RelationMBO relationMBO) {
        log.info("register : " + relationMBO);

        relationMBORepo.save(relationMBO);
    }

    @Override
    public Optional<RelationMBO> read(Long rno) {
        log.info("read : " + rno);

        return relationMBORepo.findById(rno);
    }

    @Override
    public void modify(RelationMBO relationMBO) {
        log.info("modify : " + relationMBO);

        relationMBORepo.findById(relationMBO.getRno()).ifPresent(origin -> {
            origin.setRno(relationMBO.getRno());
            origin.setEvaluated(relationMBO.getEvaluated());
            origin.setEvaluator(relationMBO.getEvaluator());
            origin.setRelation(relationMBO.getRelation());
            origin.setTno(relationMBO.getTno());
            origin.setAnswers(relationMBO.getAnswers());
            origin.setComments(relationMBO.getComments());
            origin.setFinish(relationMBO.getFinish());
            relationMBORepo.save(origin);
        });
    }

    @Override
    public void remove(Long rno) {
        log.info("remove : " + rno);

        relationMBORepo.deleteById(rno);
    }

    @Override
    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo) {
        log.info("getDistinctEvaluatedList : " + tno + vo);

        Pageable page = vo.makePageable(1, "rno");

        Page<Staff> result = null;
        String type = vo.getType();
        String keyword = vo.getKeyword();

        if (type == null || type.isEmpty()) {
            result = relationMBORepo.getDistinctEvaluatedList(tno, page);
        } else if (type.equals("evaluated")) {
            result = relationMBORepo.getDistinctEvaluatedListByEvaluated(keyword, tno, page);
        } else if (type.equals("evaluator")) {
            result = relationMBORepo.getDistinctEvaluatedListByEvaluator(keyword, tno, page);
        }
        return result;
    }

    @Override
    public Optional<List<RelationMBO>> findRelationByEvaulatedSno(long sno, long tno) {
        log.info("findRelationByEvaulatedSno " + sno);

        return relationMBORepo.findByEvaulatedSno(sno, tno);
    }

    // 회차에 속하는 평가자이면 로그인 true로 하기 위한 서비스
    @Override
    public Optional<Staff> findInEvaluator(long tno, String email) {
        return relationMBORepo.findInEvaluator(tno, email);
    }

    // 로그인 했을 때 평가할 대상자 뽑기 위한 서비스
    @Override
    public Optional<List<RelationMBO>> findByEvaluator(long sno, long tno) {
        return relationMBORepo.findByEvaulaordSno(sno, tno);
    }

    @Override
    public Optional<List<RelationMBO>> findAllbyTno(long tno) {
        return relationMBORepo.findAllbyTno(tno);
    }

    @Override
    public List<Staff> findDintinctEavluatedbyTno(long tno) {
        return relationMBORepo.findDintinctEavluatedbyTno(tno);
    }

    @Override
    public Optional<List<List<String>>> progressOfSurevey(long tno) {
        return relationMBORepo.progressOfSurevey(tno);
    };
}