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

/**
 * <code>ReplyController</code>는 목표에 달린 댓글을 REST로 관리한다.
 */
@RestController
@RequestMapping("/reply/**")
@Slf4j
@AllArgsConstructor
public class ReplyController {

    ReplyService replyService;

    /**
     * 댓글을 등록한다.
     * 
     * @param reply 댓글 정보
     * @return 댓글+상태 정보
     */
    @PostMapping("/")
    public ResponseEntity<Reply> register(@RequestBody Reply reply) {
        log.info("add object ");

        replyService.register(reply);

        Reply tmpReply = replyService.read(reply.getRno()).orElse(null);
        return new ResponseEntity<>(tmpReply, HttpStatus.OK);
    }

    /**
     * 댓글을 읽어온다.
     * 
     * @param rno 댓글 id
     * @return 댓글+상태 정보
     */
    @GetMapping("/{rno}")
    public ResponseEntity<Reply> read(@PathVariable("rno") long rno) {
        log.info("read object ");

        Reply tmpReply = replyService.read(rno).orElse(null);
        return new ResponseEntity<>(tmpReply, HttpStatus.OK);
    }

    /**
     * 댓글을 수정한다.
     * 
     * @param reply 댓글 정보
     * @return 댓글+상태 정보
     */
    @PutMapping("/")
    public ResponseEntity<Reply> modify(@RequestBody Reply reply) {
        log.info("modify " + reply);

        replyService.modify(reply);
        Reply tmpReply = replyService.read(reply.getRno()).orElse(null);
        return new ResponseEntity<>(tmpReply, HttpStatus.OK);
    }

    /**
     * 댓글을 삭제한다.
     * 
     * @param rno 댓글 id
     * @return 상태 정보
     */
    @DeleteMapping("/{rno}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("rno") long rno) {
        log.info("delete " + rno);

        replyService.remove(rno);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}