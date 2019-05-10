package com.evaluation.controller;

import java.util.Optional;

import com.evaluation.domain.Company;
import com.evaluation.domain.Staff;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

		if (staffService.read(staff.getEmail()).isPresent() == false) {
			Company company = turnService.get(tno).get().getCompany();
			staff.setCompany(company);
			staffService.register(staff);
			rttr.addFlashAttribute("msg", "success");
		} else {
			rttr.addFlashAttribute("msg", "fail");
		}

		rttr.addAttribute("tno", tno);
		return "redirect:/staff/list";
	}

	@GetMapping("/view")
	public void read(String email, long tno, PageVO vo, Model model) {
		log.info("controller : staff read by " + tno + vo);

		model.addAttribute("tno", tno);

		Optional<Staff> staff = staffService.read(email);
		Staff result = staff.get();
		model.addAttribute("staff", result);
	}

	// @GetMapping("/modify")
	// public void modify(String email, long tno, PageVO vo, Model model) {
	// log.info("controller : staff modify by " + tno + vo);

	// model.addAttribute("tno", tno);
	// model.addAttribute("page", vo.getPage());
	// model.addAttribute("size", vo.getSize());
	// model.addAttribute("type", vo.getType());
	// model.addAttribute("keyword", vo.getKeyword());

	// Optional<Staff> staff = staffService.read(email);
	// Staff result = staff.get();
	// model.addAttribute("staff", result);
	// }

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
