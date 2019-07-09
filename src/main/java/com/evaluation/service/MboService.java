package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Mbo;

/**
 * <code>MboService</code> 객체는 Mbo객체 sevice 계층의 interface를 표현한다.
 */
public interface MboService {
    /**
     * 목표를 등록한다.
     * 
     * @param mbo 목표정보
     */
    void register(Mbo mbo);

    /**
     * 목표를 읽어온다.
     * 
     * @param mno 목표 id
     * @return 목표
     */
    Optional<Mbo> read(long mno);

    /**
     * 목표를 수정한다.
     * 
     * @param mbo 목표
     */
    void modify(Mbo mbo);

    /**
     * 목표를 삭제한다.
     * 
     * @param mno 목표 id
     */
    void remove(long mno);

    /**
     * 한 회차의 한 직원의 목표를 모두 불러온다.
     * 
     * @param tno 회차 id
     * @param sno 회사 id
     * @return 목표리스트
     */
    Optional<List<Mbo>> listByTnoSno(long tno, long sno);

    /**
     * 한 회차에 속하는 특정 직원의 최종 목표 작성 비율 합을 찾는다.
     * 
     * @param tno 회차 id
     * @param sno 직원 id
     * @return 최종 목표 작성 비율을 포함한 리스트
     */
    Optional<List<List<String>>> ratioByTnoSno(long tno, long sno);

    /**
     * 한 회차의 모든 목표를 찾는다.
     * 
     * @param tno 회차 id
     * @return 회차의 모든 목표
     */
    Optional<List<List<String>>> listByTno(long tno);
}