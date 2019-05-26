package com.evaluation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evaluation.domain.Book;
import com.evaluation.service.BookService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/book/**")
@Slf4j
public class BookController {

	@Autowired
	BookService bookService;

	@GetMapping("/register")
	public void register() {

	}

	@PostMapping("/register")
	public String register(Book book, RedirectAttributes rttr) {
		log.info("register" + book);

		bookService.register(book);

		rttr.addFlashAttribute("msg", "register");
		return "redirect:/book/list";
	}

	@GetMapping("/read")
	public void read(long bno, Model model) {
		log.info("read" + bno);

		model.addAttribute("book", bookService.read(bno).get());
	}

	@PostMapping("/modify")
	public String modify(Book book, RedirectAttributes rttr) {
		log.info("modify" + book);

		bookService.modify(book);

		rttr.addFlashAttribute("msg", "modify");
		return "redirect:/book/list";
	}

	@PostMapping("/remove")
	public String remove(long bno, RedirectAttributes rttr) {
		log.info("remove " + bno);

		bookService.remove(bno);

		rttr.addFlashAttribute("msg", "remove");
		return "redirect:/book/list";
	}

	@GetMapping("/list")
	public void list(Model model) {
		List<Book> book = bookService.list();
		model.addAttribute("result", book);
	}

	@GetMapping("/contents")
	public void contents(long bno, Model model) {
		model.addAttribute("bno", bno);
		model.addAttribute("result", bookService.read(bno).get().getContents());
	}
}
