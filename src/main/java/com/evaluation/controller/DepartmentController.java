package com.evaluation.controller;

import java.util.ArrayList;
import java.util.List;

import com.evaluation.domain.Department;
import com.evaluation.domain.Staff;
import com.evaluation.domain.embeddable.Leader;
import com.evaluation.service.DepartmentService;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <code>DepartmentController</code>객체는 부문, 부서 정보를 관리한다.
 */
@Controller
@RequestMapping("/department/*")
@Slf4j
@AllArgsConstructor
public class DepartmentController {

    DepartmentService departmentService;

    TurnService turnService;

    StaffService staffService;

    /**
     * 부문, 부서 정보를 등록한다.
     * 
     * @param tno        회차 id
     * @param department 부서 정보
     * @return 상태 메시지
     */
    @PostMapping("/{tno}")
    public ResponseEntity<HttpStatus> register(@PathVariable("tno") long tno, @RequestBody Department department) {
        log.info("department register by " + tno + department);

        departmentService.register(department);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 부서 정보를 읽어온다.
     * 
     * @param tno 회차 id
     * @param dno 부서 id
     * @return 상태 메시지
     */
    @GetMapping("/{tno}/{dno}")
    @ResponseBody
    public ResponseEntity<Department> read(@PathVariable("tno") long tno, @PathVariable("dno") long dno) {
        log.info("level read by " + tno + "/" + dno);

        Department department = departmentService.read(dno).orElse(null);

        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    /**
     * 부서 정보를 수정한다.
     * 
     * @param tno        회차 id
     * @param department 부서 정보
     * @return 상태 메시지
     */
    @PutMapping("/{tno}/{dno}")
    public ResponseEntity<HttpStatus> modify(@PathVariable("tno") long tno, @RequestBody Department department) {
        log.info("modify " + department);

        departmentService.modify(department);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 부서 정보를 삭제한다.
     * 
     * @param tno  회차 id
     * @param dno  부서 id
     * @param vo   페이지 정보
     * @param rttr 재전송 정보
     * @return 부서 목록 페이지
     */
    @DeleteMapping("/{tno}/{dno}")
    public ResponseEntity<HttpStatus> remove(@PathVariable("tno") long tno, @PathVariable("dno") long dno) {
        log.info("remove " + dno);

        departmentService.remove(dno);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회차에 속하는 부서 목록을 ㄹ읽어온다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list/{tno}")
    public String readList(@PathVariable("tno") long tno, PageVO vo, Model model) {
        log.info("department list by " + tno);

        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });

        Page<Department> result = departmentService.getList(tno, vo);
        model.addAttribute("result", new PageMaker<>(result));

        List<StaffAndDno> leader = new ArrayList<StaffAndDno>();
        result.getContent().forEach(department -> {
            if (department.getLeader() != null) {
                staffService.read(department.getLeader().getSno()).ifPresent(staff -> {
                    StaffAndDno staffAndDno = new StaffAndDno();
                    staffAndDno.staff = staff;
                    staffAndDno.dno = department.getDno();
                    leader.add(staffAndDno);
                });
            }
        });
        model.addAttribute("leaderList", leader);

        return "department/list";
    }

    /**
     * <code>StaffAndDno</code> 객체는 직원정보와 부서정보를 함께 표현한다. 한명의 직원이 여러 팀을 담당하는 것을 화면에서
     * 표현하기 위해 사용.
     */
    public class StaffAndDno {
        public Staff staff;
        public Long dno;
    }

    /**
     * 부서의 리더 정보를 읽어온다. (등록 동시에)
     * 
     * @param tno   회차 id
     * @param dno   부서 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/leader/{tno}/{dno}")
    public String readLeader(@PathVariable("tno") long tno, @PathVariable("dno") long dno, PageVO vo, Model model) {
        log.info("read leader " + tno + dno);
        // dno일치하는 팀 정보와 리더 정보 전달.
        departmentService.read(dno).ifPresent(origin -> {
            if (origin.getLeader() != null) {
                model.addAttribute("team", origin.getLeader());
                staffService.read(origin.getLeader().getSno()).ifPresent(staff -> {
                    model.addAttribute("leader", staff);
                });
            }
        });

        // leader를 전체 직원 중 등록하기 위해 직원 명단 전송
        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
            staffService.findByCno(origin.getCno()).ifPresent(staff -> {
                model.addAttribute("staffList", staff);
            });
        });

        model.addAttribute("dno", dno);
        model.addAttribute("page", vo.getPage());
        model.addAttribute("size", vo.getSize());
        model.addAttribute("type", vo.getType());
        model.addAttribute("keyword", vo.getKeyword());

        return "department/leader";
    }

    /**
     * 리더를 등록한다.
     * 
     * @param tno    회차 id
     * @param dno    부서 id
     * @param leader 리더 정보
     * @return 부서 목록 페이지
     */
    @PutMapping("/leader/{tno}/{dno}")
    public ResponseEntity<HttpStatus> registerLeader(@PathVariable("tno") long tno, @PathVariable("dno") long dno,
            @RequestBody Leader leader) {
        log.info("register leader " + tno + dno + leader);
        departmentService.read(dno).ifPresent(origin -> {
            origin.setLeader(leader);
            departmentService.modify(origin);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
