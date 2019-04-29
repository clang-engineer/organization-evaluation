package com.evaluation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evaluation.domain.Company;
import com.evaluation.persistence.CompanyRepository;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/company/**")
@Slf4j
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepo;

	@GetMapping("/list")
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {

		Pageable page = vo.makePageable(0, "cno");

		Page<Company> result = companyRepo.findAll(companyRepo.makePredicate(vo.getType(), vo.getKeyword()), page);

		model.addAttribute("result", new PageMaker<>(result));
	}

	@GetMapping("/register")
	public void registerGET() {
		log.info("register get");
	}

	@PostMapping("/register")
	public String registerPost(Company vo, RedirectAttributes rttr) {
		log.info("register post");

		companyRepo.save(vo);
		rttr.addFlashAttribute("msg", "success");

		return "redirect:/company/list";
	}

	@GetMapping("/view")
	public void view(Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("" + cno);

		companyRepo.findById(cno).ifPresent(company -> model.addAttribute("vo", company));

	}

	@GetMapping("/modify")
	public void modifyGet(Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("" + cno);

		companyRepo.findById(cno).ifPresent(company -> model.addAttribute("vo", company));
	}

	@PostMapping("/modify")
	public String modifyPost(Company company, PageVO vo, RedirectAttributes rttr) {
		log.info("" + company);

		companyRepo.findById(company.getCno()).ifPresent(origin -> {
			origin.setId(company.getId());
			origin.setName(company.getName());
			origin.setPassword(company.getPassword());
			origin.setHomepage(company.getHomepage());
			companyRepo.save(origin);
			rttr.addFlashAttribute("msg", "success");
			rttr.addAttribute("cno", origin.getCno());
		});

		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/company/view";
	}

	@PostMapping("/delete")
	public String delete(Long cno, PageVO vo, RedirectAttributes rttr) {
		companyRepo.deleteById(cno);
		rttr.addFlashAttribute("msg", "success");
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect/company/list";

	}

	@GetMapping("/surveyList")
	public void surveyList(Company company, PageVO vo, RedirectAttributes rttr) {

	}
}
