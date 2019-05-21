package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Staff;
import com.evaluation.service.StaffService;

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

    @GetMapping("/staff/list/{cno}/{tno}")
    public ResponseEntity<List<Staff>> getStaffListEntity(@PathVariable("cno") long cno,
            @PathVariable("tno") long tno) {
        log.info("getList....");
        return new ResponseEntity<>(staffService.getAllListExcludeEvalated(cno, tno), HttpStatus.OK);
    }
}