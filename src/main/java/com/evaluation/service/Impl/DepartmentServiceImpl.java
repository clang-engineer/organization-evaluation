package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Department;
import com.evaluation.persistence.DepartmentRepository;
import com.evaluation.service.DepartmentService;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Setter(onMethod_ = { @Autowired })
    private DepartmentRepository departmentRepo;

    @Override
    public void register(Department department) {
        log.info("register " + department);

        departmentRepo.save(department);
    }

    public Optional<Department> read(long dno) {
        log.info("read " + dno);

        Optional<Department> result = departmentRepo.findById(dno);
        return result;
    }

    @Override
    public void modify(Department department) {
        log.info("modify " + department);

        departmentRepo.findById(department.getDno()).ifPresent(origin -> {
            origin.setDepartment1(department.getDepartment1());
            origin.setDepartment2(department.getDepartment2());
            origin.setUpdateId(department.getUpdateId());
            departmentRepo.save(origin);
        });
    }

    @Override
    public void remove(long dno) {
        log.info("remove " + dno);

        departmentRepo.deleteById(dno);
    }

    @Override
    public Page<Department> getListWithPaging(long tno, PageVO vo) {
        log.info("getListWithPaging by " + tno);

        Pageable page = vo.makePageable(1, "dno");
        Page<Department> result = departmentRepo
                .findAll(departmentRepo.makePredicate(vo.getType(), vo.getKeyword(), tno), page);
        return result;
    }

    @Override
    public Optional<List<Department>> findByTnoSno(long tno, long sno) {
        return departmentRepo.findByTnoSno(tno, sno);
    }

    @Override
    public Optional<Department> findByDepartment(long tno, String department1, String department2) {
        log.info("get dep by " + tno + "/" + department1 + "/" + department2);

        return departmentRepo.findByDeparment(tno, department1, department2);
    }
}
