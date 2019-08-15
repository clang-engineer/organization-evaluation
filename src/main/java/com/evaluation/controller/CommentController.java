package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Turn;
import com.evaluation.service.TurnService;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

/**
 * <code>CommentController</code>객체는 주관식 문항 정보를 관리한다.
 */
@Controller
@RequestMapping("/turns/{tno}")
@Slf4j
public class CommentController {

    @Autowired
    TurnService turnService;

    /**
     * 주관식 정보를 등록한다.
     * 
     * @param tno     회차 id
     * @param comment 주관식 문항
     * @return 상태 메시지
     */
    @PostMapping("/comments")
    public ResponseEntity<HttpStatus> register(@PathVariable("tno") long tno, @RequestBody String comment) {
        log.info("add " + comment);

        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().add(comment);
            turnService.register(turn);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 주관식 정보를 수정한다.
     * 
     * @param tno     회차 id
     * @param idx     리스트 index
     * @param comment 주관식 문항
     * @return 상태 메시지
     */
    @PutMapping("/comments/{idx}")
    public ResponseEntity<HttpStatus> modify(@PathVariable("tno") long tno, @PathVariable("idx") int idx,
            @RequestBody String comment) {
        log.info("Modify " + comment);

        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().set(idx, comment);
            turnService.register(turn);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 주관식 정보를 읽어온다.
     * 
     * @param tno 회차 id
     * @param idx 리스트 index
     * @return 주관식 정보
     */
    @GetMapping("/comments/{idx}")
    public ResponseEntity<String> read(@PathVariable("tno") long tno, @PathVariable("idx") int idx) {
        log.info("read " + tno + "/" + idx);

        List<String> comments = turnService.read(tno).map(Turn::getComments).orElse(null);

        return new ResponseEntity<>(comments.get(idx), HttpStatus.OK);
    }

    /**
     * 주관식 문항을 삭제한다.
     * 
     * @param tno 회차 id
     * @param idx 리스트 index
     * @return 상태 메시지
     */
    @DeleteMapping("/comments/{idx}")
    public ResponseEntity<HttpStatus> remove(@PathVariable("tno") long tno, @PathVariable("idx") int idx) {
        log.info("remove " + idx);
        turnService.read(tno).ifPresent(turn -> {
            turn.getComments().remove(idx);
            turnService.register(turn);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회차에 속하는 주관식 목록을 전달한다.
     * 
     * @param tno   회차 id
     * @param model 화면 전달 정보
     * @return 주관식 리스트 화면
     */
    @GetMapping("/comments/list")
    public String readList(@PathVariable("tno") long tno, Model model) {
        log.info("question list by " + tno);

        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });
        return "comment/list";
    }
}