package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Mbo;
import com.evaluation.persistence.MboRepository;
import com.evaluation.service.MboService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class MboServiceImpl implements MboService {

    MboRepository mboRepo;

    @Override
    public void register(Mbo mbo) {
        log.info("register " + mbo);

        mboRepo.save(mbo);
    }

    @Override
    public Optional<Mbo> read(long mno) {
        log.info("read " + mno);

        return mboRepo.findById(mno);
    }

    @Override
    public void modify(Mbo mbo) {
        log.info("modify " + mbo);

        mboRepo.findById(mbo.getMno()).ifPresent(origin -> {
            origin.setObject(mbo.getObject());
            origin.setProcess(mbo.getProcess());
            origin.setRatio(mbo.getRatio());
            origin.setFinish(mbo.getFinish());
            origin.setUpdateId(mbo.getUpdateId());

            mboRepo.save(origin);
        });
    }

    @Override
    public void remove(long mno) {
        log.info("remove " + mno);

        mboRepo.findById(mno).ifPresent(origin -> {
            mboRepo.deleteById(origin.getMno());
        });
    }

    @Override
    public Optional<List<Mbo>> listByTnoSno(long tno, long sno) {
        log.info("list by " + tno + "/" + sno);

        return mboRepo.listByTnoSno(tno, sno);
    }

    @Override
    public Optional<List<List<String>>> ratioByTnoSno(long tno, long sno) {
        log.info("ratio by" + tno + sno);

        return mboRepo.ratioByTnoSno(tno, sno);
    }

    @Override
    public Optional<List<List<String>>> listByTno(long tno) {
        log.info("list by tno : " + tno);
        return mboRepo.listByTno(tno);
    }
}