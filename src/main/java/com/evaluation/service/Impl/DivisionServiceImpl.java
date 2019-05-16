package com.evaluation.service.Impl;

import java.util.Optional;

import com.evaluation.domain.Division;
import com.evaluation.persistence.DivisionRepository;
import com.evaluation.service.DivisionService;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DivisionServiceImpl implements DivisionService {

    @Setter(onMethod_ = { @Autowired })
    private DivisionRepository divisionRepo;

    @Override
    public void register(Division division) {
        log.info("register " + division);

        divisionRepo.save(division);
    }

    public Optional<Division> read(long dno) {
        log.info("read " + dno);

        Optional<Division> result = divisionRepo.findById(dno);
        return result;
    }

    @Override
    public void modify(Division division) {
        log.info("modify " + division);

        divisionRepo.save(division);
    }

    @Override
    public void remove(long dno) {
        log.info("remove " + dno);

        divisionRepo.deleteById(dno);
    }

    @Override
    public Page<Division> getListWithPaging(long cno, PageVO vo) {
        log.info("getListWithPaging by " + cno);

        Pageable page = vo.makePageable(1, "dno");
        Page<Division> result = divisionRepo.findAll(divisionRepo.makePredicate(vo.getType(), vo.getKeyword(), cno),
                page);
        return result;
    }
}
