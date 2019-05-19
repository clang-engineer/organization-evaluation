package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Relation360;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

public interface Relation360Service {

    public void register(Relation360 relation360);

    public Optional<Relation360> read(Long rno);

    public void modify(Relation360 relation360);

    public void remove(Long rno);

    public Page<Relation360> getList(long tno, PageVO vo);

}