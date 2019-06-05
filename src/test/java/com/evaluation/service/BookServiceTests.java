package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class BookServiceTests {

    @Setter(onMethod_ = { @Autowired })
    private BookService bookService;

    @Setter(onMethod_ = { @Autowired })
    private InfoSurveyService info360Service;

    @Test
    public void contentsList() {
        Optional<Book> book = bookService.read(1);

        book.get().getContents().forEach(content -> log.info("" + content));
    }

    @Test
    public void name() {

        // log.info(bookService.read(Long.parseLong(info360Service.read(1L).getReplyCode()).get().getTitle());
        log.info(bookService.read(info360Service.read(7).getReplyCode()).get().getTitle());
    }
}