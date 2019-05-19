package com.evaluation.service.Impl;

import java.util.Optional;

import com.evaluation.domain.Relation360;
import com.evaluation.persistence.Relation360Repository;
import com.evaluation.service.Relation360Service;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Relation360ServiceImpl implements Relation360Service {

    @Setter(onMethod_ = { @Autowired })
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
    public Page<Relation360> getList(long tno, PageVO vo) {
        log.info("getList : " + tno + vo);

        Pageable page = vo.makePageable(1, "rno");

        Page<Relation360> result = relation360Repo
                .findAll(relation360Repo.makePredicate(vo.getType(), vo.getKeyword(), tno), page);

        return result;
    }
}