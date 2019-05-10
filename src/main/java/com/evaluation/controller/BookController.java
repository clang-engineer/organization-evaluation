package com.evaluation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evaluation.domain.Book;
import com.evaluation.service.BookService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/book/**")
@Slf4j
public class BookController {

	@Autowired
	BookService bookService;

	@GetMapping("/list")
	public void list(Model model) {
		List<Book> book = bookService.list();
		model.addAttribute("result", book);
	}
}
