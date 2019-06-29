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

@RestController
@RequestMapping("/contents/*")
@Slf4j
public class ContentController {

    @Autowired
    private BookService bookService;

    @PostMapping("/{bno}")
    public ResponseEntity<List<Content>> addContent(@PathVariable("bno") int bno, @RequestBody Content content) {
        log.info("add Content");

        Book book = bookService.read(bno).get();
        List<Content> contents = book.getContents();
        contents.add(content);
        book.setContents(contents);

        bookService.register(book);

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.CREATED);
    }

    @GetMapping("/{bno}/{idx}")
    public ResponseEntity<Content> read(@PathVariable("bno") int bno, @PathVariable("idx") int idx) {
        Content content = bookService.read(bno).get().getContents().get(idx);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @PutMapping("/{bno}/{idx}")
    public ResponseEntity<List<Content>> modify(@PathVariable("bno") int bno, @PathVariable("idx") int idx,
            @RequestBody Content content) {
        log.info("modify Content");

        Book book = bookService.read(bno).get();
        List<Content> contents = book.getContents();
        contents.set(idx, content);
        book.setContents(contents);

        bookService.modify(book);

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.CREATED);
    }

    @DeleteMapping("/{bno}/{idx}")
    public ResponseEntity<List<Content>> delete(@PathVariable("bno") int bno, @PathVariable("idx") int idx) {
        log.info("delete Content");

        Book book = bookService.read(bno).get();
        List<Content> contents = book.getContents();
        contents.remove(idx);
        book.setContents(contents);

        bookService.modify(book);

        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.OK);
    }

    @GetMapping("/{bno}")
    public ResponseEntity<List<Content>> getContents(@PathVariable("bno") int bno) {
        log.info("get contents");
        return new ResponseEntity<>(getContentsByBook(bno), HttpStatus.OK);
    }

    private List<Content> getContentsByBook(int bno) {
        return bookService.read(bno).get().getContents();
    }

}
