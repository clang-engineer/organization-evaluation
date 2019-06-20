package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.MBO;
import com.evaluation.persistence.MBORepository;
import com.evaluation.service.MBOService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class MBOServiceImpl implements MBOService {

    MBORepository mboRepo;

    @Override
    public void register(MBO mbo) {
        log.info("register " + mbo);

        mboRepo.save(mbo);
    }

    @Override
    public Optional<MBO> read(long mno) {
        log.info("read " + mno);

        return mboRepo.findById(mno);
    }

    @Override
    public void modify(MBO mbo) {
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

        mboRepo.deleteById(mno);
    }

    @Override
    public Optional<List<MBO>> listByTnoSno(long tno, long sno) {
        log.info("list by " + tno + "/" + sno);

        return mboRepo.listByTnoSno(tno, sno);
    }
}