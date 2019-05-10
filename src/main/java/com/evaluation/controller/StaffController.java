package com.evaluation.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evaluation.domain.Company;
import com.evaluation.domain.Staff;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/staff/*")
@Slf4j
public class StaffController {

	@Setter(onMethod_ = { @Autowired })
	StaffService staffService;

	@Setter(onMethod_ = { @Autowired })
	TurnService turnService;

	@GetMapping("/register")
	public void register(long tno, PageVO vo, Model model) {
		log.info("controller : staff register get by " + tno + vo);

		model.addAttribute("tno", tno);
	}

	@PostMapping("/register")
	public String register(Staff staff, long tno, RedirectAttributes rttr) {
		log.info("controller : staff register post by " + tno);

		Company company = turnService.get(tno).get().getCompany();
		staff.setCompany(company);
		staffService.register(staff);
		rttr.addFlashAttribute("msg", "success");

		rttr.addAttribute("tno", tno);
		return "redirect:/staff/list";
	}

	@GetMapping("/view")
	public void read(long sno, long tno, PageVO vo, Model model) {
		log.info("controller : staff read by " + tno + vo);

		model.addAttribute("tno", tno);

		Optional<Staff> staff = staffService.read(sno);
		Staff result = staff.get();
		model.addAttribute("staff", result);
	}

	@GetMapping("/modify")
	public void modify(long sno, long tno, PageVO vo, Model model) {
		log.info("controller : staff modify by " + tno + vo);

		model.addAttribute("tno", tno);

		Optional<Staff> staff = staffService.read(sno);
		Staff result = staff.get();
		model.addAttribute("staff", result);
	}

	@PostMapping("/modify")
	public String modify(Staff staff, long tno, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : staff modify post by " + staff.getName());

		staffService.read(staff.getSno()).ifPresent(origin -> {
			origin.setEmail(staff.getEmail());
			origin.setName(staff.getName());
			origin.setId(staff.getId());
			origin.setPassword(staff.getPassword());
			origin.setDepartment1(staff.getDepartment1());
			origin.setDepartment2(staff.getDepartment2());
			origin.setLevel(staff.getLevel());
			origin.setDivision1(staff.getDivision1());
			origin.setDivision2(staff.getDivision2());
			staffService.modify(origin);
			rttr.addFlashAttribute("msg", "success");
		});

		rttr.addAttribute("tno", tno);
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());
		return "redirect:/staff/list";
	}

	// @GetMapping
	// public void remove() {
	// }

	@GetMapping("/list")
	public void readList(long tno, PageVO vo, Model model) {
		log.info("controller : staff list by " + tno + vo);

		model.addAttribute("tno", tno);

		long cno = turnService.get(tno).get().getCompany().getCno();
		Page<Staff> result = staffService.getList(cno, vo);
		model.addAttribute("result", new PageMaker<>(result));
	}

}
