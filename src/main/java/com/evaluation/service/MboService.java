package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Mbo;

public interface MboService {
    public void register(Mbo mbo);

    public Optional<Mbo> read(long mno);

    public void modify(Mbo mbo);

    public void remove(long mno);

    public Optional<List<Mbo>> listByTnoSno(long tno, long sno);

    public Optional<List<List<String>>> ratioByTnoSno(long tno, long sno);

    public Optional<List<List<String>>> listByTno(long tno);
}