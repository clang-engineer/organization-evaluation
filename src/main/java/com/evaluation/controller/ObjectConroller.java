package com.evaluation.controller;

import java.util.Optional;

import com.evaluation.domain.MBO;
import com.evaluation.service.MBOService;
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

// 목표 REST하기 위한 컨트롤러!!
@RestController
@RequestMapping("/object/**")
@Slf4j
@AllArgsConstructor
public class ObjectConroller {

    MBOService mboService;

    ReplyService replyService;

    @PostMapping("/")
    public ResponseEntity<MBO> register(@RequestBody MBO mbo) {
        log.info("add object ");

        mboService.register(mbo);
        return new ResponseEntity<>(mbo, HttpStatus.OK);
    }

    @GetMapping("/{mno}")
    public ResponseEntity<MBO> read(@PathVariable("mno") long mno) {
        log.info("add object ");

        MBO mbo = Optional.ofNullable(mboService.read(mno)).map(Optional::get).orElse(null);
        return new ResponseEntity<>(mbo, HttpStatus.OK);
    }

    @PutMapping("/{mno}/{step}")
    public ResponseEntity<HttpStatus> modify(@PathVariable("mno") long mno, @PathVariable("step") String step,
            @RequestBody MBO mbo) {
        log.info("modify " + step);

        // plan 단계가 아니면 기존 객체 복사해서 finish M으로 등록해놈, 기록 남김! 수정은 M 삭제는 D
        if (!step.equals("plan")) {
            mboService.read(mno).ifPresent(origin -> {
                try {
                    MBO tmp = (MBO) origin.clone();
                    tmp.setMno(0);
                    tmp.setFinish("M");
                    mboService.register(tmp);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 단계가 어찌됐던 수정은 함
        mboService.modify(mbo);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{mno}/{step}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("mno") long mno, @PathVariable("step") String step) {
        log.info("delete " + mno);

        // plan 단계가 아니면 기존 객체 복사해서 finish D으로 등록해놈, 기록 남김! 수정은 M 삭제는 D
        if (!step.equals("plan")) {
            mboService.read(mno).ifPresent(origin -> {
                try {
                    MBO tmp = (MBO) origin.clone();
                    tmp.setMno(0);
                    tmp.setFinish("D");
                    mboService.register(tmp);

                    // 댓글 mno 수정하기
                    replyService.listByMno(mno).ifPresent(list -> {
                        list.forEach(reply -> {
                            reply.setMno(tmp.getMno());
                            replyService.modify(reply);
                        });
                    });
                    log.info("================>" + tmp.getMno());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 동록 후에는 해당 mno원본 삭제함
        mboService.remove(mno);

        // 단계가 어디든 게시물이 삭제되면 댓글은 삭제되도록 함.
        replyService.listByMno(mno).ifPresent(list -> {
            list.forEach(reply -> {
                replyService.remove(reply.getRno());
            });
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

}