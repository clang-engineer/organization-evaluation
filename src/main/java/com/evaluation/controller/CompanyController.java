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

/**
 * <code>CommentController</code>객체는 회사 정보를 관리한다.
 */
@Controller
@RequestMapping("/company/*")
@Slf4j
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	/**
	 * 회사 목록을 전달한다.
	 * 
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/list")
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company list by " + vo);

		Page<Company> result = companyService.getList(vo);
		model.addAttribute("result", new PageMaker<>(result));
	}

	/**
	 * 회사 등록 화면을 표현한다.
	 */
	@GetMapping("/register")
	public void registerGET() {
		log.info("controller : company register get");
	}

	/**
	 * 회사를 등록한다.
	 * 
	 * @param vo   회사 정보
	 * @param rttr 재전송 정보
	 * @return 회사 목록
	 */
	@PostMapping("/register")
	public String registerPost(Company vo, RedirectAttributes rttr) {
		log.info("controller : company register post " + vo);

		companyService.register(vo);
		rttr.addFlashAttribute("msg", "register");

		return "redirect:/company/list";
	}

	/**
	 * 회사 정보를 읽어온다.
	 * 
	 * @param cno   회사 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/view")
	public void view(Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company view get" + cno);

		companyService.read(cno).ifPresent(company -> model.addAttribute("vo", company));
	}

	/**
	 * 회사 정보 수정 페이지를 표현한다.
	 * 
	 * @param cno   회사 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/modify")
	public void modify(Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company modfiy get " + cno);

		companyService.read(cno).ifPresent(company -> model.addAttribute("vo", company));
	}

	/**
	 * 회사 정보를 수정한다.
	 * 
	 * @param company 회사 정보
	 * @param vo      페이지 정보
	 * @param rttr    재전송 정보
	 * @return 회사 정보 페이지
	 */
	@PostMapping("/modify")
	public String modify(Company company, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : company modify post" + company);

		companyService.modify(company);

		rttr.addFlashAttribute("msg", "modify");
		rttr.addAttribute("cno", company.getCno());

		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/company/view";
	}

	/**
	 * 회사 정보를 삭제한다.
	 * 
	 * @param cno  회사 id
	 * @param vo   페이지 정보
	 * @param rttr 재전송 정보
	 * @return 회사 목록
	 */
	@PostMapping("/delete")
	public String delete(Long cno, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : company delete " + cno);

		companyService.remove(cno);

		rttr.addFlashAttribute("msg", "remove");
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/company/list";

	}

	/**
	 * 회사에 속한 회차 목록을 표현한다.
	 * 
	 * @param cno   회사 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/turnList")
	public void turnList(@ModelAttribute("cno") Long cno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("controller : company surveyList");

		companyService.read(cno).ifPresent(company -> model.addAttribute("vo", company));
	}
}
