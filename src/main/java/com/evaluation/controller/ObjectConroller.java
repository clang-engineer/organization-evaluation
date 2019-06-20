package com.evaluation.controller;

import com.evaluation.domain.MBO;
import com.evaluation.service.MBOService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/object/**")
@Slf4j
@AllArgsConstructor
public class ObjectConroller {

    MBOService mboService;

    @PostMapping("/")
    public ResponseEntity<HttpStatus> register(@RequestBody MBO mbo) {
        log.info("add object ");

        mboService.register(mbo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}