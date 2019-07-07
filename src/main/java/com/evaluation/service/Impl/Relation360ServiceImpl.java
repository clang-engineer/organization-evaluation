package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.persistence.Relation360Repository;
import com.evaluation.service.Relation360Service;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Relation360ServiceImpl implements Relation360Service {

    @Autowired
    Relation360Repository relation360Repo;

    @Override
    public void register(Relation360 relation360) {
        log.info("register : " + relation360);

        relation360Repo.save(relation360);
    }

    @Override
    public Optional<Relation360> read(Long rno) {
        log.info("read : " + rno);

        return relation360Repo.findById(rno);
    }

    @Override
    public void modify(Relation360 relation360) {
        log.info("modify : " + relation360);

        relation360Repo.findById(relation360.getRno()).ifPresent(origin -> {
            origin.setRno(relation360.getRno());
            origin.setEvaluated(relation360.getEvaluated());
            origin.setEvaluator(relation360.getEvaluator());
            origin.setRelation(relation360.getRelation());
            origin.setTno(relation360.getTno());
            origin.setAnswers(relation360.getAnswers());
            origin.setComments(relation360.getComments());
            origin.setFinish(relation360.getFinish());
            relation360Repo.save(origin);
        });
    }

    @Override
    public void remove(Long rno) {
        log.info("remove : " + rno);

        relation360Repo.deleteById(rno);
    }

    @Override
    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo) {
        log.info("getDistinctEvaluatedList : " + tno + vo);

        Pageable page = vo.makePageable(1, "rno");

        Page<Staff> result = null;
        String type = vo.getType();
        String keyword = vo.getKeyword();

        if (type == null || type.isEmpty()) {
            result = relation360Repo.getDistinctEvaluatedList(tno, page);
        } else if (type.equals("evaluated")) {
            result = relation360Repo.getDistinctEvaluatedListByEvaluated(tno, keyword, page);
        } else if (type.equals("evaluator")) {
            result = relation360Repo.getDistinctEvaluatedListByEvaluator(tno, keyword, page);
        }
        return result;
    }

    @Override
    public Optional<List<Relation360>> findByEvaulated(long tno, long sno) {
        log.info("findByEvaulated " + sno);

        return relation360Repo.findByEvaulated(tno, sno);
    }

    // 회차에 속하는 평가자이면 로그인 true로 하기 위한 서비스
    @Override
    public Optional<Staff> findByTnoAndEvaluator(long tno, String email) {
        return relation360Repo.findByTnoAndEvaluator(tno, email);
    }

    // 로그인 했을 때 평가할 대상자 뽑기 위한 서비스
    @Override
    public Optional<List<Relation360>> findByEvaluator(long tno, long sno) {
        return relation360Repo.findByEvaulator(tno, sno);
    }

    @Override
    public Optional<List<Relation360>> findAllbyTno(long tno) {
        return relation360Repo.findAllbyTno(tno);
    }

    @Override
    public List<Staff> findDintinctEavluatedbyTno(long tno) {
        return relation360Repo.findDintinctEavluatedbyTno(tno);
    }

    @Override
    public Optional<List<List<String>>> progressOfSurevey(long tno) {
        return relation360Repo.progressOfSurevey(tno);
    };
}