package com.evaluation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * @param rttr       재전송 정보
     * @return 부서 목록 페이지
     */
    @PostMapping("/register")
    public String register(long tno, Department department, RedirectAttributes rttr) {
        log.info("department register by " + tno + department);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        department.setTno(tno);
        departmentService.register(department);

        return "redirect:/department/list";
    }

    /**
     * 부서 정보를 수정한다.
     * 
     * @param tno        회차 id
     * @param department 부서 정보
     * @param vo         페이지 정보
     * @param rttr       재전송 정보
     * @return 부서 목록 페이지
     */
    @PostMapping("/modify")
    public String modify(long tno, Department department, PageVO vo, RedirectAttributes rttr) {
        log.info("modify " + department);

        departmentService.modify(department);
        rttr.addFlashAttribute("msg", "modify");

        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/department/list";
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
    @PostMapping("/remove")
    public String remove(long tno, long dno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + dno);

        departmentService.remove(dno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/department/list";
    }

    /**
     * 회차에 속하는 부서 목록을 ㄹ읽어온다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("department list by " + tno);

        model.addAttribute("tno", tno);

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
    @GetMapping("/leader")
    public void readLeader(long tno, long dno, PageVO vo, Model model) {
        // dno일치하는 팀 정보와 리더 정보 전달.
        departmentService.read(dno).ifPresent(department -> {
            if (department.getLeader() != null) {
                model.addAttribute("team", department.getLeader());
                staffService.read(department.getLeader().getSno()).ifPresent(staff -> {
                    model.addAttribute("leader", staff);
                });
            }
        });

        // leader를 전체 직원 중 등록하기 위해 직원 명단 전송
        turnService.read(tno).ifPresent(turn -> {
            long cno = turn.getCno();
            staffService.findByCno(cno).ifPresent(origin -> {
                model.addAttribute("staffList", origin);
            });
        });

        model.addAttribute("dno", dno);
        model.addAttribute("tno", tno);
        model.addAttribute("page", vo.getPage());
        model.addAttribute("size", vo.getSize());
        model.addAttribute("type", vo.getType());
        model.addAttribute("keyword", vo.getKeyword());
    }

    /**
     * 리더를 등록한다.
     * 
     * @param tno    회차 id
     * @param dno    부서 id
     * @param leader 리더 정보
     * @param vo     페이지 정보
     * @param rttr   재전송 정보
     * @return 부서 목록 페이지
     */
    @PostMapping("/leader/register")
    public String registerLeader(long tno, long dno, Leader leader, PageVO vo, RedirectAttributes rttr) {

        departmentService.read(dno).ifPresent(origin -> {
            origin.setLeader(leader);
            departmentService.modify(origin);
        });

        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/department/list";
    }

    /**
     * 부서 정보를 읽어오는 REST 
     *(Mbo 목표 작성 시 부서 정보 읽어오기 위해)
     * 
     * @param dno 부서 id
     * @return 부서 정보
     */
    @GetMapping("/{dno}")
    @ResponseBody
    public ResponseEntity<Department> read(@PathVariable("dno") long dno) {
        Department department = Optional.ofNullable(departmentService.read(dno)).map(Optional::get).orElse(null);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    /**
     * 부서 정보 등록 하는 REST 
     *(Mbo에서 팀장이 팀 목표 등록시)
     * 
     * @param dno    부서 id
     * @param leader 리더 정보
     * @return http 상태 정보
     */
    @PutMapping("/{dno}")
    @ResponseBody
    public ResponseEntity<HttpStatus> modify(@PathVariable("dno") long dno, @RequestBody Leader leader) {

        departmentService.read(dno).ifPresent(origin -> {
            origin.getLeader().setTitle(leader.getTitle());
            origin.getLeader().setContent(leader.getContent());
            departmentService.modify(origin);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
