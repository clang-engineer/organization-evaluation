package com.evaluation.controller;

import java.util.Optional;

import com.evaluation.domain.MBO;
import com.evaluation.service.MBOService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{mno}")
    public ResponseEntity<MBO> register(@PathVariable("mno") long mno) {
        log.info("add object ");

        MBO mbo = Optional.ofNullable(mboService.read(mno)).map(Optional::get).orElse(null);
        return new ResponseEntity<>(mbo, HttpStatus.OK);
    }

    @PutMapping("/{mno}")
    public ResponseEntity<HttpStatus> modify(@PathVariable("mno") long mno, @RequestBody MBO mbo) {
        log.info("modify " + mno);

        mboService.modify(mbo);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{mno}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("mno") long mno) {
        log.info("delete " +  mno);

        mboService.remove(mno);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}