package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Staff;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/rest")
@RestController
@AllArgsConstructor
@Slf4j
public class RestListController {
    private StaffService staffService;
    private TurnService turnService;

    @GetMapping("/staff/evaluated/{tno}")
    public ResponseEntity<List<Staff>> getStaffForEvaluated(@PathVariable("tno") long tno) {
        log.info("get All Staff List Exclude Evaluated....");

        long cno = turnService.get(tno).get().getCompany().getCno();
        return new ResponseEntity<>(staffService.getEvaluatedList(cno, tno), HttpStatus.OK);
    }

    @GetMapping("/staff/evaluator/{tno}/{sno}")
    public ResponseEntity<List<Staff>> getStaffForEvaluator(@PathVariable("tno") long tno,
            @PathVariable("sno") long sno) {
        log.info("get All Staff List....");

        long cno = turnService.get(tno).get().getCompany().getCno();
        return new ResponseEntity<>(staffService.getEvaluatorList(cno, tno, sno), HttpStatus.OK);
    }

}