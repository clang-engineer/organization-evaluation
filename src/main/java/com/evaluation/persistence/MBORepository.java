package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.MBO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MBORepository extends CrudRepository<MBO, Long> {

    // 해당 turn에 속하는 직원의 목표 리스트
    @Query("SELECT m FROM MBO m WHERE m.tno=:tno AND m.sno=:sno")
    public Optional<List<MBO>> listByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

}