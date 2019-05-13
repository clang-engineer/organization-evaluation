package com.evaluation.controller;

import java.util.List;

import com.evaluation.domain.Book;
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

@RestController
@RequestMapping("/contents/*")
@Slf4j
public class ContentController {

    @Autowired
    private BookService bookService;

    @PostMapping("/{bno}")
    public ResponseEntity<List<String>> addContent(@PathVariable("bno") long bno, @RequestBody String content) {
        log.info("add Content");

        Book book = bookService.read(bno).get();
        List<String> contents = book.getContents();
        contents.add(content);
        book.setContents(contents);

        bookService.register(book);

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.CREATED);
    }

    @PutMapping("/{bno}/{idx}")
    public ResponseEntity<List<String>> modify(@PathVariable("bno") long bno, @PathVariable("idx") int idx,
            @RequestBody String content) {
        log.info("modify Content");

        Book book = bookService.read(bno).get();
        List<String> contents = book.getContents();
        contents.set(idx, content);
        book.setContents(contents);

        bookService.modify(book);

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.CREATED);

    }

    @DeleteMapping("/{bno}/{idx}")
    public ResponseEntity<List<String>> delete(@PathVariable("bno") long bno, @PathVariable("idx") int idx) {
        log.info("delete Content");

        Book book = bookService.read(bno).get();
        List<String> contents = book.getContents();
        contents.remove(idx);
        book.setContents(contents);

        bookService.modify(book);

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.OK);
    }

    @GetMapping("/{bno}")
    public ResponseEntity<List<String>> getContents(@PathVariable("bno") long bno) {
        log.info("get contents");
        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.OK);
    }

    private List<String> getContentsByBook(long bno) {
        return bookService.read(bno).get().getContents();
    }
}
