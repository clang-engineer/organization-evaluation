package com.evaluation.controller;

import com.evaluation.domain.Level;
import com.evaluation.service.LevelService;
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
 * <code>LevelController 직급 정보를 관리한다.</code>
 */
@Controller
@RequestMapping("/level/*")
@Slf4j
public class LevelController {

    @Autowired
    LevelService levelService;

    @Autowired
    TurnService turnService;

    /**
     * 직급 정보를 등록한다.
     * 
     * @param tno   회차 id
     * @param level 직급 정보
     * @return 상태 메시지
     */
    @PostMapping("/{tno}")
    public ResponseEntity<HttpStatus> register(@PathVariable("tno") long tno, @RequestBody Level level) {
        log.info("level register by " + tno + level);

        turnService.read(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            level.setCno(cno);
            levelService.register(level);
        });
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 직급 정보를 읽어온다.
     * 
     * @param tno 회차 id
     * @param lno 직급 id
     * @return 상태 메시지
     */
    @GetMapping("/{tno}/{lno}")
    @ResponseBody
    public ResponseEntity<Level> read(@PathVariable("tno") long tno, @PathVariable("lno") long lno) {
        log.info("level read by " + tno + "/" + lno);

        Level level = levelService.read(lno).orElse(null);

        return new ResponseEntity<>(level, HttpStatus.OK);
    }

    /**
     * 직급 정보를 수정한다.
     * 
     * @param tno   회차 id
     * @param level 직급 정보
     * @param vo    페이지 정보
     * @return 직급 목록 페이지
     */
    @PutMapping("/{tno}/{lno}")
    @ResponseBody
    public ResponseEntity<Level> modify(@PathVariable("tno") long tno, @RequestBody Level level) {
        log.info("modify " + level);

        levelService.modify(level);

        return new ResponseEntity<>(level, HttpStatus.OK);
    }

    /**
     * 직급 정보를 삭제한다.
     * 
     * @param tno 회차 id
     * @param lno 직급 id
     * @return 직급 목록 페이지
     */
    @DeleteMapping("/{tno}/{lno}")
    public ResponseEntity<HttpStatus> remove(@PathVariable("tno") long tno, @PathVariable("lno") long lno) {
        log.info("remove " + lno);

        levelService.remove(lno);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 직급 목록을 읽어온다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list/{tno}")
    public String readList(@PathVariable("tno") long tno, PageVO vo, Model model) {
        log.info("level list by " + tno);

        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);

            Page<Level> result = levelService.getList(origin.getCno(), vo);
            model.addAttribute("result", new PageMaker<>(result));
        });

        return "level/list";
    }

}
