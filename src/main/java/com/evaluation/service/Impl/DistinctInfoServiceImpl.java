package com.evaluation.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evaluation.persistence.DistinctInfoRepository;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.DistinctInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class DistinctInfoServiceImpl implements DistinctInfoService {

    @Autowired
    DistinctInfoRepository repo;

    @Autowired
    TurnRepository turnRepo;

    @Override
    public List<String> getListDepartment1(long cno) {
        return repo.getListDepartment1(cno);
    }

    @Override
    public List<String> getListDepartment2(long cno) {
        return repo.getListDepartment2(cno);
    }

    @Override
    public List<String> getListDivision1(long cno) {
        return repo.getListDivision1(cno);
    }

    @Override
    public List<String> getListDivision2(long cno) {
        return repo.getListDivision2(cno);
    }

    @Override
    public List<String> getListLevel(long cno) {
        return repo.getListLevel(cno);
    }

    @Override
    public List<String> getListCategory(long tno) {
        return repo.getListCategory(tno);
    }

    @Override
    public Map<String, Object> getDistinctInfo(long tno) {
        long cno = turnRepo.findById(tno).get().getCompany().getCno();

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("department1", repo.getListDepartment1(cno));
        result.put("department2", repo.getListDepartment2(cno));
        result.put("division1", repo.getListDivision1(cno));
        result.put("division2", repo.getListDivision2(cno));
        result.put("level", repo.getListLevel(cno));

        return result;
    }

    @Override
    public Map<String, Object> getDistinctQuestionInfo(long tno) {
        long cno = turnRepo.findById(tno).get().getCompany().getCno();

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("division1", repo.getListDivision1(cno));
        result.put("division2", repo.getListDivision2(cno));
        result.put("category", repo.getListCategory(tno));

        return result;
    }
}