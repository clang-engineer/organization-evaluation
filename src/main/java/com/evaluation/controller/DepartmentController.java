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

@Controller
@RequestMapping("/department/*")
@Slf4j
@AllArgsConstructor
public class DepartmentController {

    DepartmentService departmentService;

    TurnService turnService;

    StaffService staffService;

    @PostMapping("/register")
    public String register(long tno, Department department, RedirectAttributes rttr) {
        log.info("department register by " + tno + department);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);

        department.setTno(tno);

        departmentService.register(department);

        return "redirect:/department/list";
    }

    @PostMapping("/modify")
    public String modify(Department department, long tno, PageVO vo, RedirectAttributes rttr) {
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

    @PostMapping("/remove")
    public String remove(long dno, long tno, PageVO vo, RedirectAttributes rttr) {
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

    // leader인 스태프 전달하기 위해 내부 객체 사용! 한명의 직원이 여러 팀을 담당하는 것을 표현하기 위해 사용.
    public class StaffAndDno {
        public Staff staff;
        public Long dno;
    }

    @GetMapping("/leader")
    public void readLeader(long dno, long tno, PageVO vo, Model model) {
        //dno일치하는 팀 정보와 리더 정보 전달.
        departmentService.read(dno).ifPresent(department -> {
            if (department.getLeader() != null) {
                model.addAttribute("team", department.getLeader());
                staffService.read(department.getLeader().getSno()).ifPresent(staff -> {
                    model.addAttribute("leader", staff);
                });
            }
        });

        //leader를 전체 직원 명단 전송
        long cno = turnService.read(tno).get().getCno();
        staffService.readBycno(cno).ifPresent(origin -> {
            model.addAttribute("staffList", origin);
        });

        model.addAttribute("dno", dno);
        model.addAttribute("tno", tno);
        model.addAttribute("page", vo.getPage());
        model.addAttribute("size", vo.getSize());
        model.addAttribute("type", vo.getType());
        model.addAttribute("keyword", vo.getKeyword());
    }

    @PostMapping("/leader/register")
    public String registerLeader(Leader leader, long dno, long tno, PageVO vo, RedirectAttributes rttr) {

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

    @GetMapping("/{dno}")
    @ResponseBody
    public ResponseEntity<Department> read(@PathVariable("dno") long dno) {
        Department department = Optional.ofNullable(departmentService.read(dno)).map(Optional::get).orElse(null);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

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
