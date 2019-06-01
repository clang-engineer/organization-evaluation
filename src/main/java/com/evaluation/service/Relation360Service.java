package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface Relation360Service {

    public void register(Relation360 relation360);

    public Optional<Relation360> read(Long rno);

    public void modify(Relation360 relation360);

    public void remove(Long rno);

    public List<Relation360> getAllList(long tno);

    public Page<Relation360> getListWithPaging(long tno, PageVO vo);

    public Page<Staff> getDistinctEvaluatedList(long tno, PageVO vo);

    public void deleteEvaluatedInfo(long tno, long sno);

    public void deleteAllRelationByTno(long tno);

    public List<Relation360> findRelationByEvaulatedSno(long sno);
}