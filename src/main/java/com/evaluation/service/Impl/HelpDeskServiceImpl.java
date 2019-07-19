package com.evaluation.service.Impl;

import java.util.Optional;

import com.evaluation.domain.HelpDesk;
import com.evaluation.persistence.HelpDeskRepository;
import com.evaluation.service.HelpDeskService;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class HelpDeskServiceImpl implements HelpDeskService {

    private HelpDeskRepository helpDeskRepo;

    @Override
    public void register(HelpDesk helpDesk) {
        log.info("service : help register" + helpDesk);

        helpDeskRepo.save(helpDesk);
    }

    @Override
    public Optional<HelpDesk> read(long hno) {
        log.info("service : help read" + hno);

        return helpDeskRepo.findById(hno);
    }

    @Override
    public void modify(HelpDesk helpDesk) {
        log.info("service : help modify " + helpDesk);
        helpDeskRepo.findById(helpDesk.getHno()).ifPresent(origin -> {
            origin.setSurveyInfo(helpDesk.getSurveyInfo());
            origin.setCompany(helpDesk.getCompany());
            origin.setName(helpDesk.getName());
            origin.setEmail(helpDesk.getEmail());
            origin.setTelephone(helpDesk.getTelephone());
            origin.setContent(helpDesk.getContent());
            origin.setComplete(helpDesk.getComplete());
            origin.setUpdateId(helpDesk.getUpdateId());
            helpDeskRepo.save(origin);
        });
    }

    @Override
    public void remove(long hno) {
        log.info("service : help remove " + hno);
        helpDeskRepo.findById(hno).ifPresent(origin -> {
            helpDeskRepo.deleteById(origin.getHno());
        });
    }

    @Override
    public Page<HelpDesk> getList(PageVO vo) {
        log.info("service : helpdesk getList by" + vo);

        Pageable page = vo.makePageable(0, "hno");
        Page<HelpDesk> result = helpDeskRepo.findAll(helpDeskRepo.makePredicate(vo.getType(), vo.getKeyword()), page);
        return result;
    };
}