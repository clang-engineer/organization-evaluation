package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.MBO;

public interface MBOService {
    public void register(MBO mbo);

    public Optional<MBO> read(long mno);

    public void modify(MBO mbo);

    public void remove(long mno);

    public Optional<List<MBO>> listByTnoSno(long tno, long sno);

    public Optional<List<List<String>>> ratioByTnoSno(long tno, long sno);

    public Optional<List<List<String>>> listByTno(long tno);
}