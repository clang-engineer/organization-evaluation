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
        Optional<List<Mbo>> listByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

        /**
         * 회차 속하는 전인원이 작성한 목표를 찾는다. (엑셀 다운)
         * 
         * @param tno 회차id
         * @return 직원 목표 리스트의 전인원 리스트
         */
        @Query(value = "select s.name, s.email, s.level, s.department1, s.department2, m.finish, m.object, m.process, m.ratio from tbl_mbo m left join tbl_staff s on m.staff_sno=s.sno where m.turn_tno=:tno order by m.mno asc", nativeQuery = true)
        Optional<List<List<String>>> listByTno(@Param("tno") long tno);

        /**
         * 한 회차에 속하는 특정 직원의 최종 목표 작성 비율 합을 찾는다.
         * 
         * @param tno 회차 id
         * @param sno 직원 idz
         * @return 직원 목표가 들어있는 리스트의 전인원 리스트
         */
        @Query(value = "select staff_sno,turn_tno,sum(ratio) from tbl_mbo where finish='Y' and turn_tno=:tno and staff_sno=:sno group by staff_sno", nativeQuery = true)
        Optional<List<List<String>>> ratioByTnoSno(@Param("tno") long tno, @Param("sno") long sno);

        /**
         * recentChangeOfEvaluatedList를 위한 쿼리문 mysql8 방식
         */
        String mysql8String = "SELECT r.rno, r.name, u.index, u.type, u.write_id, u.write_date, u.update_date "
                        + "FROM (SELECT r.rno, r.evaluated, s.name, r.evaluator FROM tbl_relation_mbo r "
                        + "LEFT JOIN tbl_staff s ON r.evaluated=s.sno "
                        + "WHERE r.turn_tno=:tno AND r.evaluator=:sno) r " + "LEFT JOIN "
                        + "((SELECT m.staff_sno, RANK() OVER (PARTITION BY m.staff_sno ORDER BY m.mno) AS 'index', 'object' AS 'type', m.write_id, m.write_date, m.update_date "
                        + "FROM evaluation.tbl_mbo m " + "WHERE turn_tno=:tno AND finish='Y') " + "UNION ALL "
                        + "(SELECT m.staff_sno, DENSE_RANK() OVER (PARTITION BY m.staff_sno ORDER BY m.mno) AS 'index', 'reply' AS 'type', r.write_id, r.write_date, r.update_date "
                        + "FROM tbl_mbo_reply r " + "RIGHT JOIN tbl_mbo m ON r.mbo_mno=m.mno "
                        + "WHERE turn_tno=:tno AND finish='Y')) u " + "ON r.evaluated=u.staff_sno "
                        + "WHERE u.write_id IS NOT NULL AND NOT (r.evaluated=:sno AND u.type='object') ORDER BY u.update_date DESC LIMIT 5 OFFSET :offset";

        /**
         * recentChangeOfEvaluatedList를 위한 쿼리문 mysql5 방식
         * 
         * : 문자 삽입에러가 나서 \\ 넣어줌!
         * mysql5에서 그룹 랭킹을 우회하는 방법으로 문제를 품.
         * @see https://blackbull.tistory.com/43
         */
        String recentChangeOfEvaluatedList = "SELECT r.rno, r.name, u.index, u.type, u.write_id, u.write_date, u.update_date FROM "
                        + "(SELECT r.rno, r.evaluated, s.name, r.evaluator FROM tbl_relation_mbo r "
                        + "LEFT JOIN tbl_staff s ON r.evaluated=s.sno WHERE r.turn_tno=:tno AND r.evaluator=:sno) r "
                        + "LEFT JOIN "
                        + "((SELECT staff_sno, rnum as 'index', 'object' AS 'type', write_id, write_date, update_date "
                        + "FROM (SELECT m.*, IF(@vsno=m.staff_sno AND @vmno!=m.mno, @rownum\\:=@rownum+1, @rownum\\:=1) rnum, (@vsno\\:=m.staff_sno) vsno, (@vmno\\:=m.mno) vmno FROM evaluation.tbl_mbo m, (SELECT @vsno\\:=0, @vmno\\:=0, @rownum\\:=0 FROM DUAL) b "
                        + "WHERE turn_tno=:tno AND finish='Y' ORDER BY m.staff_sno, m.mno) c) " + "UNION ALL "
                        + "(SELECT staff_sno, rnum as 'index', 'reply' AS 'type', write_id, write_date, update_date FROM ( "
                        + "SELECT m.*, "
                        + "CASE WHEN @vsno=m.staff_sno AND @vmno!=m.mno THEN @rownum\\:=@rownum+1 WHEN @vsno=m.staff_sno AND @vmno=m.mno THEN @rownum\\:=@rownum WHEN @vsno!=m.staff_sno THEN @rownum\\:=1 "
                        + "END AS rnum, (@vsno\\:=m.staff_sno) vsno, (@vmno\\:=m.mno) vmno "
                        + "FROM (SELECT m.staff_sno, m.mno, r.write_id, r.write_date, r.update_date FROM tbl_mbo_reply r RIGHT JOIN tbl_mbo m ON r.mbo_mno=m.mno WHERE m.turn_tno=1 AND m.finish='Y' ORDER BY m.staff_sno, m.mno) m, "
                        + "(SELECT @vsno\\:=0, @vmno\\:=0, @rownum\\:=0 FROM DUAL) b) c)) u "
                        + "ON r.evaluated=u.staff_sno WHERE u.write_id IS NOT NULL AND NOT (r.evaluated=:sno AND u.type='object') ORDER BY u.update_date DESC LIMIT 5 OFFSET :offset";

        /**
         * 평가 관계인의 목표나 댓글 목록을 찾는다.
         * 
         * 1. 댓글과 목표를 조인해서 댓글에 직원 번호를 입힌다. (댓글에 직원 번호가 없으므로) 
         * 2. 회차 내에 목표와 댓글에 목표 번호를 입힌 후 리스트를 합친다.(최종 Y만 해당) 
         * 3. 관계 정보에 직원 정보를 조인해서 이름을 찾는다. 
         * 4. 3과 2를 조인한다.
         * 
         * @param tno    회차 id
         * @param sno    평가자 id
         * @param offset 페이지 offset
         * @return 목표, 댓글 변동 목록
         */
        @Query(value = recentChangeOfEvaluatedList, nativeQuery = true)
        Optional<List<String>> recentChangeOfEvaluatedList(@Param("tno") long tno, @Param("sno") long sno,
                        @Param("offset") int offset);
}