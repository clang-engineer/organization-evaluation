package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Book;
import com.evaluation.domain.embeddable.Content;
import com.evaluation.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

/**
 * <code>ContentController</code>객체는 Book에 속한 회답 정보를 REST로 관리한다.
 */
@RestController
@RequestMapping("/contents/*")
@Slf4j
public class ContentController {

    @Autowired
    private BookService bookService;

    /**
     * 회답 정보를 등록한다.
     * 
     * @param bno     book id
     * @param content 회답
     * @return 회답 목록
     */
    @PostMapping("/{bno}")
    public ResponseEntity<List<Content>> addContent(@PathVariable("bno") int bno, @RequestBody Content content) {
        log.info("add Content");

        bookService.read(bno).ifPresent(origin -> {
            List<Content> contents = origin.getContents();
            contents.add(content);
            origin.setContents(contents);
            bookService.register(origin);
        });

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.CREATED);
    }

    /**
     * 회답 정보를 읽어온다.
     * 
     * @param bno book id
     * @param idx 회답 index
     * @return 회답 정보
     */
    @GetMapping("/{bno}/{idx}")
    public ResponseEntity<Content> read(@PathVariable("bno") int bno, @PathVariable("idx") int idx) {
        log.info("list Content");

        Content content = bookService.read(bno).map(Book::getContents).map(list -> list.get(idx)).orElse(null);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    /**
     * 회답 정보를 수정한다.
     * 
     * @param bno     book id
     * @param idx     회답 index
     * @param content 회답
     * @return 회답 목록
     */
    @PutMapping("/{bno}/{idx}")
    public ResponseEntity<List<Content>> modify(@PathVariable("bno") int bno, @PathVariable("idx") int idx,
            @RequestBody Content content) {
        log.info("modify Content");

        bookService.read(bno).ifPresent(origin -> {
            List<Content> contents = origin.getContents();
            contents.set(idx, content);
            origin.setContents(contents);
            bookService.modify(origin);
        });

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.CREATED);
    }

    /**
     * 회답 정보를 삭제한다.
     * 
     * @param bno book id
     * @param idx 회답 index
     * @return 회답 목록
     */
    @DeleteMapping("/{bno}/{idx}")
    public ResponseEntity<List<Content>> delete(@PathVariable("bno") int bno, @PathVariable("idx") int idx) {
        log.info("delete Content");

        bookService.read(bno).ifPresent(origin -> {
            List<Content> contents = origin.getContents();
            contents.remove(idx);
            origin.setContents(contents);
            bookService.modify(origin);
        });

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.OK);
    }

    /**
     * 회답 목록을 읽어온다.
     * 
     * @param bno book id
     * @return 회답 목록
     */
    @GetMapping("/{bno}")
    public ResponseEntity<List<Content>> getContents(@PathVariable("bno") int bno) {
        log.info("get contents");
        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.OK);
    }

    /**
     * 회답 목록을 읽어오는 함수
     * 
     * @param bno book id
     * @return 회답 목록
     */
    private List<Content> getContentsByBook(int bno) {
        return bookService.read(bno).map(Book::getContents).orElse(null);
    }

}
