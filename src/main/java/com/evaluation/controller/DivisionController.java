package com.evaluation.controller;

import com.evaluation.domain.Division;
import com.evaluation.service.DivisionService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

/**
 * * <code>DivisionController</code>객체는 직군, 계층 정보를 관리한다.
 */
@Controller
@RequestMapping("/division/*")
@Slf4j
public class DivisionController {

    @Autowired
    DivisionService divisionService;

    @Autowired
    TurnService turnService;

    /**
     * 계층 정보를 등록한다.
     * 
     * @param tno      회차 id
     * @param division 계층 정보
     * @return 상태 메시지
     */
    @PostMapping("/{tno}")
    @ResponseBody
    public ResponseEntity<HttpStatus> register(@PathVariable("tno") long tno, @RequestBody Division division) {
        log.info("division register by " + tno + division);

        turnService.read(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            division.setCno(cno);
            divisionService.register(division);
        });

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 계층 정보를 읽어온다.
     * 
     * @param tno 회차 id
     * @param dno 계층 id
     * @return 상태 메시지
     */
    @GetMapping("/{tno}/{dno}")
    @ResponseBody
    public ResponseEntity<Division> read(@PathVariable("tno") long tno, @PathVariable("dno") long dno) {
        log.info("level read by " + tno + "/" + dno);

        Division division = divisionService.read(dno).orElse(null);

        return new ResponseEntity<>(division, HttpStatus.OK);
    }

    /**
     * 계층 정보를 수정한다.
     * 
     * @param tno      회차 id
     * @param division 계층 정보
     * @return 상태 메시지
     */
    @PutMapping("/{tno}/{dno}")
    public ResponseEntity<Division> modify(@PathVariable("tno") long tno, @RequestBody Division division) {
        log.info("modify " + division);

        divisionService.modify(division);

        return new ResponseEntity<>(division, HttpStatus.OK);
    }

    /**
     * 계층 정보를 삭제한다.
     * 
     * @param tno 회차 id
     * @param dno 계층 id
     * @return 상태 메시지
     */
    @DeleteMapping("/{tno}/{dno}")
    public ResponseEntity<HttpStatus> remove(@PathVariable("tno") long tno, @PathVariable("dno") long dno) {
        log.info("remove " + dno);

        divisionService.remove(dno);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 계층 목록을 읽어온다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list/{tno}")
    public String readList(@PathVariable("tno") long tno, PageVO vo, Model model) {
        log.info("division list by " + tno);

        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);

            Page<Division> result = divisionService.getList(origin.getCno(), vo);
            model.addAttribute("result", new PageMaker<>(result));
        });

        return "division/list";
    }

}
