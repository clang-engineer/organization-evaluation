package com.evaluation.service.Impl;

import java.util.HashMap;
import java.util.Map;

import com.evaluation.persistence.DepartmentRepository;
import com.evaluation.persistence.DivisionRepository;
import com.evaluation.persistence.LevelRepository;
import com.evaluation.persistence.QuestionRepository;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.DistinctInfoService;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class DistinctInfoServiceImpl implements DistinctInfoService {

    TurnRepository turnRepo;

    DepartmentRepository departmentRepo;

    DivisionRepository divisionRepo;

    LevelRepository levelRepo;

    QuestionRepository questionRepo;

    @Override
    public Map<String, Object> getDistinctInfo(long tno) {
        long cno = turnRepo.findById(tno).get().getCno();

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("department1", departmentRepo.getListDepartment1(tno));
        result.put("department2", departmentRepo.getListDepartment2(tno));
        result.put("division1", divisionRepo.getListDivision1(cno));
        result.put("division2", divisionRepo.getListDivision2(cno));
        result.put("level", levelRepo.getListLevel(cno));

        return result;
    }

    @Override
    public Map<String, Object> getDistinctQuestionInfo(long tno) {
        long cno = turnRepo.findById(tno).get().getCno();

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("division1", divisionRepo.getListDivision1(cno));
        result.put("division2", divisionRepo.getListDivision2(cno));
        result.put("category", questionRepo.getListCategory(tno));

        return result;
    }
}