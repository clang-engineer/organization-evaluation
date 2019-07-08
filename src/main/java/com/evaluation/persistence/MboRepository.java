package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Mbo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * <code>MboRepository</code> 객체는 Mbo 객체의 영속화를 위해 표현한다.
 */
public interface MboRepository extends CrudRepository<Mbo, Long> {

    /**
     * 해당 회차에 속하는 한 직원의 목표 리스트
     * 
     * @param tno 회차id
     * @param sno 직원id
     * @return 목표리스트
     */
    @Query("SELECT m FROM Mbo m WHERE m.tno=:tno AND m.sno=:sno ORDER BY m.mno ASC")
    public Optional<List<Mbo>> listByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

    /**
     * 회차 속하는 전인원이 작성한 목표를 찾는다. (엑셀 다운)
     * 
     * @param tno 회차id
     * @return 직원 목표 리스트의 전인원 리스트
     */
    @Query(value = "select s.name, s.email, s.level, s.department1, s.department2, m.finish, m.object, m.process, m.ratio from tbl_mbo m left join tbl_staff s on m.staff_sno=s.sno where m.turn_tno=:tno order by m.mno asc", nativeQuery = true)
    public Optional<List<List<String>>> listByTno(@Param("tno") long tno);

    /**
     * 한 회차에 속하는 특정 직원의 최종 목표 작성 비율 합을 찾는다.
     * 
     * @param tno 회차 id
     * @param sno 직원 id
     * @return 직원 목표가 들어있는 리스트의 전인원 리스트
     */
    @Query(value = "select staff_sno,turn_tno,sum(ratio) from tbl_mbo where finish='Y' group by staff_sno having turn_tno=:tno and staff_sno=:sno", nativeQuery = true)
    public Optional<List<List<String>>> ratioByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

}