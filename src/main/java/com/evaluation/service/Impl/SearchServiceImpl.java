package com.evaluation.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evaluation.persistence.SearchRepository;
import com.evaluation.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SearchServiceImpl implements SearchService {

    @Autowired
    SearchRepository searchRepo;

    @Override
    public List<String> getListDepartment1(long cno) {
        return searchRepo.getListDepartment1(cno);
    }

    @Override
    public List<String> getListDepartment2(long cno) {
        return searchRepo.getListDepartment2(cno);
    }

    @Override
    public List<String> getListDivision1(long cno) {
        return searchRepo.getListDivision1(cno);
    }

    @Override
    public List<String> getListDivision2(long cno) {
        return searchRepo.getListDivision2(cno);
    }

    @Override
    public List<String> getListLevel(long cno) {
        return searchRepo.getListLevel(cno);
    }

    @Override
    public Map<String, Object> getDistinctInfo(long cno) {

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("department1", searchRepo.getListDepartment1(cno));
        result.put("department2", searchRepo.getListDepartment2(cno));
        result.put("division1", searchRepo.getListDivision1(cno));
        result.put("division2", searchRepo.getListDivision2(cno));
        result.put("level", searchRepo.getListLevel(cno));

        return result;
    }

}