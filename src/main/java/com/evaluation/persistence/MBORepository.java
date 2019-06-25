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

    // 해당 tno와 sno에 속하는 멤버의 ratio 합계
    // @Query("SELECT m.sno, SUM(m.ratio) FROM MBO m GROUP BY m.sno HAVING (m.tno=:tno AND m.sno=:sno)")
    // 위 jpql에서 계속 컬럼 인식 문제가 나타남(turn_tno) 때문에 nativeQuery를 통해 우회.
    @Query(value = "select staff_sno,turn_tno,sum(ratio) from tbl_mbo where finish='Y' group by staff_sno having turn_tno=:tno and staff_sno=:sno",nativeQuery = true)
    public Optional<List<List<String>>> ratioByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

}