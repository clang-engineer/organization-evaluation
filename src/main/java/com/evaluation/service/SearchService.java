package com.evaluation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SearchService {

    public List<String> getListDepartment1(long cno);

    public List<String> getListDepartment2(long cno);

    public List<String> getListDivision1(long cno);

    public List<String> getListDivision2(long cno);

    public List<String> getListLevel(long cno);

    public Map<String, Object> getDistinctInfo(long cno);

}