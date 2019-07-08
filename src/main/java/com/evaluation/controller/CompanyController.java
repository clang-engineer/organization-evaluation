package com.evaluation.controller;

import com.evaluation.domain.Company;
import com.evaluation.service.CompanyService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/company/**")
@Slf4j
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@GetMapping("/list")
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company list by " + vo);

		Page<Company> result = companyService.getList(vo);
		model.addAttribute("result", new PageMaker<>(result));
	}

	@GetMapping("/register")
	public void registerGET() {
		log.info("controller : company register get");
	}

	@PostMapping("/register")
	public String registerPost(Company vo, RedirectAttributes rttr) {
		log.info("controller : company register post " + vo);

		companyService.register(vo);
		rttr.addFlashAttribute("msg", "register");

		return "redirect:/company/list";
	}

	@GetMapping("/view")
	public void view(Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company view get" + cno);

		companyService.read(cno).ifPresent(company -> model.addAttribute("vo", company));
	}

	@GetMapping("/modify")
	public void modify(Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company modfiy get " + cno);

		companyService.read(cno).ifPresent(company -> model.addAttribute("vo", company));
	}

	@PostMapping("/modify")
	public String modify(Company company, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : company modify post" + company);

		companyService.read(company.getCno()).ifPresent(origin -> {
			companyService.modify(origin);

			rttr.addFlashAttribute("msg", "modify");
			rttr.addAttribute("cno", company.getCno());
		});

		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/company/view";
	}

	@PostMapping("/delete")
	public String delete(Long cno, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : company delete " + cno);

		companyService.read(cno).ifPresent(origin -> {
			companyService.remove(origin.getCno());
		});

		rttr.addFlashAttribute("msg", "remove");
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/company/list";

	}

	@GetMapping("/turnList")
	public void turnList(@ModelAttribute("cno") Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company surveyList");

		companyService.read(cno).ifPresent(company -> model.addAttribute("vo", company));
	}
}
