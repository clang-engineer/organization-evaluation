package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.Company;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistinctInfoRepository extends CrudRepository<Company, Long> {

    @Query("SELECT DISTINCT d.department1 FROM Department d WHERE cno=?1 ORDER BY d.department1 ASC")
    public List<String> getListDepartment1(long cno);

    @Query("SELECT DISTINCT d.department2 FROM Department d WHERE cno=?1 ORDER BY d.department2 ASC")
    public List<String> getListDepartment2(long cno);

    @Query("SELECT DISTINCT d.division1 FROM Division d WHERE cno=?1 ORDER BY d.division1 ASC")
    public List<String> getListDivision1(long cno);

    @Query("SELECT DISTINCT d.division2 FROM Division d WHERE cno=?1 ORDER BY d.division2 ASC")
    public List<String> getListDivision2(long cno);

    @Query("SELECT DISTINCT l.content FROM Level l WHERE cno=?1 ORDER BY l.content ASC")
    public List<String> getListLevel(long cno);

    @Query("SELECT DISTINCT q.category FROM Question q WHERE turn.tno=?1 ORDER BY q.category ASC")
    public List<String> getListCategory(long tno);
}
