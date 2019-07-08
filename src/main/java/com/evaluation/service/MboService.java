package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Mbo;

public interface MboService {
    void register(Mbo mbo);

    Optional<Mbo> read(long mno);

    void modify(Mbo mbo);

    void remove(long mno);

    Optional<List<Mbo>> listByTnoSno(long tno, long sno);

    Optional<List<List<String>>> ratioByTnoSno(long tno, long sno);

    Optional<List<List<String>>> listByTno(long tno);
}