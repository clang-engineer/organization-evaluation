package com.evaluation.controller;

import com.evaluation.domain.Reply;
import com.evaluation.service.ReplyService;

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
@RequestMapping("/reply/**")
@Slf4j
@AllArgsConstructor
public class ReplyController {

    ReplyService replyService;

    @PostMapping("/")
    public ResponseEntity<Reply> register(@RequestBody Reply reply) {
        log.info("add object ");

        replyService.register(reply);

        return new ResponseEntity<>(replyService.read(reply.getRno()).get(), HttpStatus.OK);
    }

    @GetMapping("/{rno}")
    public ResponseEntity<Reply> read(@PathVariable("rno") long rno) {
        log.info("read object ");

        return new ResponseEntity<>(replyService.read(rno).get(), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Reply> modify(@RequestBody Reply reply) {
        log.info("modify " + reply);

        replyService.modify(reply);

        return new ResponseEntity<>(replyService.read(reply.getRno()).get(), HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("rno") long rno) {
        log.info("delete " + rno);

        replyService.remove(rno);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}