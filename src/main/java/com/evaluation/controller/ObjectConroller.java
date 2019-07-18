package com.evaluation.controller;

import java.util.Optional;

import com.evaluation.domain.Department;
import com.evaluation.domain.Mbo;
import com.evaluation.domain.embeddable.Leader;
import com.evaluation.service.DepartmentService;
import com.evaluation.service.MboService;
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
 * <code>ObjectConroller</code>객체는 목표를 REST로 관리한다.
 * 
 * path 포괄을 위해 ** 을 사용..
 */
@RestController
@RequestMapping("/object/**")
@Slf4j
@AllArgsConstructor
public class ObjectConroller {

    MboService mboService;

    ReplyService replyService;

    DepartmentService departmentService;

    /**
     * 목표를 등록한다.
     * 
     * @param mbo 목표 정보
     * @return 목표 정보
     */
    @PostMapping("/")
    public ResponseEntity<Mbo> register(@RequestBody Mbo mbo) {
        log.info("add object ");

        mboService.register(mbo);
        return new ResponseEntity<>(mbo, HttpStatus.OK);
    }

    /**
     * 목표를 읽는다.
     * 
     * @param mno 목표 id
     * @return 목표 정보
     */
    @GetMapping("/{mno}")
    public ResponseEntity<Mbo> read(@PathVariable("mno") long mno) {
        log.info("add object ");

        Mbo mbo = Optional.ofNullable(mboService.read(mno)).map(Optional::get).orElse(null);
        return new ResponseEntity<>(mbo, HttpStatus.OK);
    }

    /**
     * 목표를 수정한다.
     * 
     * @param mno  목표 id
     * @param step 단계
     * @param mbo  목표
     * @return http 상태
     */
    @PutMapping("/{mno}/{step}")
    public ResponseEntity<HttpStatus> modify(@PathVariable("mno") long mno, @PathVariable("step") String step,
            @RequestBody Mbo mbo) {
        log.info("modify " + step);

        // plan 단계가 아니면 기존 객체 복사해서 finish M으로 등록해놈, 기록 남김! 수정은 M 삭제는 D
        if (!step.equals("plan")) {
            mboService.read(mno).ifPresent(origin -> {
                try {
                    // 객체를 복사하고, 수정되는 것을 막기 위해 mno를 0으로 셋팅한다.
                    Mbo tmp = (Mbo) origin.clone();
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

    /**
     * 목표를 삭제한다.
     * 
     * @param mno  목표 id
     * @param step 단계
     * @return http 상태 정보
     */
    @DeleteMapping("/{mno}/{step}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("mno") long mno, @PathVariable("step") String step) {
        log.info("delete " + mno);

        // plan 단계가 아니면 기존 객체 복사해서 finish D으로 등록해놈, 기록 남김! 수정은 M 삭제는 D
        if (!step.equals("plan")) {
            mboService.read(mno).ifPresent(origin -> {
                try {
                    Mbo tmp = (Mbo) origin.clone();
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

        /**
     * 부서 정보를 읽어오는 REST (Mbo 목표 작성 시 부서 정보 읽어오기 위해)
     * 
     * @param dno 부서 id
     * @return 부서 정보
     */
    @GetMapping("/department/{dno}")
    public ResponseEntity<Department> teamObjectRead(@PathVariable("dno") long dno) {
        log.info("read leader " + dno);
        Department department = departmentService.read(dno).orElse(null);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    /**
     * 부서 정보 등록 하는 REST (Mbo에서 팀장이 팀 목표 등록시)
     * 
     * @param dno    부서 id
     * @param leader 리더 정보
     * @return http 상태 정보
     */
    @PutMapping("/department/{dno}")
    public ResponseEntity<HttpStatus> teamObjectModify(@PathVariable("dno") long dno, @RequestBody Leader leader) {
        log.info("modify leader info " + dno);
        departmentService.read(dno).ifPresent(origin -> {
            if (origin.getLeader() != null) {
                origin.getLeader().setTitle(leader.getTitle());
                origin.getLeader().setContent(leader.getContent());
                departmentService.modify(origin);
            }
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}