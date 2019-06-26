package com.evaluation.service;

import java.util.List;
import java.util.Map;

public interface DistinctInfoService {

    public List<String> getListDepartment1(long tno);

    public List<String> getListDepartment2(long tno);

    public List<String> getListDivision1(long cno);

    public List<String> getListDivision2(long cno);

    public List<String> getListLevel(long cno);

    public Map<String, Object> getDistinctInfo(long tno);
    
    public List<String> getListCategory(long cno);
    
    public Map<String, Object> getDistinctQuestionInfo(long tno);
}