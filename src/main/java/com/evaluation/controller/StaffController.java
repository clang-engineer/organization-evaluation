package com.evaluation.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.evaluation.domain.Department;
import com.evaluation.domain.Division;
import com.evaluation.domain.Level;
import com.evaluation.domain.Staff;
import com.evaluation.function.AboutExcel;
import com.evaluation.function.RandomPassword;
import com.evaluation.service.DepartmentService;
import com.evaluation.service.DistinctInfoService;
import com.evaluation.service.DivisionService;
import com.evaluation.service.LevelService;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/staff/*")
@Slf4j
@AllArgsConstructor
@Transactional
public class StaffController {

	StaffService staffService;

	TurnService turnService;

	DistinctInfoService distinctInfoService;

	LevelService levelService;

	DepartmentService departmentService;

	DivisionService divisionService;

	Relation360Service relation360Service;

	@GetMapping("/register")
	public void register(long tno, PageVO vo, Model model) {
		log.info("controller : staff register get by " + tno + vo);

		model.addAttribute("distinctInfo", distinctInfoService.getDistinctInfo(tno));
		model.addAttribute("tno", tno);
	}

	@PostMapping("/register")
	public String register(Staff staff, long tno, RedirectAttributes rttr) {
		log.info("controller : staff register post by " + tno);

		long cno = turnService.get(tno).get().getCno();
		staff.setCno(cno);
		staffService.register(staff);

		rttr.addFlashAttribute("msg", "register");
		rttr.addAttribute("tno", tno);
		return "redirect:/staff/list";
	}

	@GetMapping("/view")
	public void read(long sno, long tno, PageVO vo, Model model) {
		log.info("controller : staff read by " + tno + vo);

		Optional<Staff> staff = staffService.read(sno);

		model.addAttribute("tno", tno);
		Staff result = staff.get();
		model.addAttribute("staff", result);
	}

	@GetMapping("/modify")
	public void modify(long sno, long tno, PageVO vo, Model model) {
		log.info("controller : staff modify by " + tno + vo);

		Optional<Staff> staff = staffService.read(sno);
		Staff result = staff.get();

		model.addAttribute("distinctInfo", distinctInfoService.getDistinctInfo(tno));
		model.addAttribute("tno", tno);
		model.addAttribute("staff", result);
	}

	@PostMapping("/modify")
	public String modify(Staff staff, long tno, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : staff modify post by " + staff.getName());

		staffService.modify(staff);

		rttr.addFlashAttribute("msg", "modify");

		rttr.addAttribute("tno", tno);
		rttr.addAttribute("sno", staff.getSno());
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/staff/view";
	}

	@PostMapping("/remove")
	public String remove(long sno, long tno, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : staff delete by " + sno);

		staffService.remove(sno);

		rttr.addAttribute("tno", tno);
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());
		rttr.addFlashAttribute("msg", "remove");
		return "redirect:/staff/list";
	}

	@GetMapping("/list")
	public void readList(long tno, PageVO vo, Model model) {
		log.info("controller : staff list by " + tno + vo);

		model.addAttribute("tno", tno);

		long cno = turnService.get(tno).get().getCno();
		Page<Staff> result = staffService.getList(cno, vo);
		model.addAttribute("result", new PageMaker<>(result));
	}

	@PostMapping("/upload")
	@ResponseBody
	public void xlUpload(long tno, Staff staff, Boolean deleteList, MultipartFile uploadFile, Model model) {

		log.info("read file" + uploadFile);
		log.info("" + deleteList);

		long cno = turnService.get(tno).get().getCno();
		if (deleteList == true) {
			// 관계 모두 삭제하고 등록
			relation360Service.deleteAllRelationByTno(tno);
			staffService.deleteByCno(cno);
		}

		int iteration = 0;
		List<List<String>> allData = AboutExcel.readExcel(uploadFile);

		// 중복 제거 부서
		Set<String> levList = new TreeSet<String>();
		Set<List<String>> depList = new HashSet<List<String>>();
		Set<List<String>> divList = new HashSet<List<String>>();
		// 직원 등록
		for (List<String> list : allData) {
			if (iteration == 0) {
				iteration++;
				continue;
			}
			log.info("" + list);
			Staff row = new Staff();
			row.setCno(cno);
			row.setName(list.get(0));
			row.setId(list.get(1));
			row.setLevel(list.get(2));
			row.setDepartment1(list.get(3));
			row.setDepartment2(list.get(4));
			row.setDivision1(list.get(5));
			row.setDivision2(list.get(6));
			row.setEmail(list.get(7));
			if (list.get(8) == null || list.get(8) == "N") {
				row.setPassword(RandomPassword.numberGen(6, 2));
			} else {
				row.setPassword(list.get(8));
			}
			row.setTelephone(list.get(9));
			row.setWriteId(staff.getWriteId());
			row.setUpdateId(staff.getUpdateId());

			staffService.register(row);

			// 중복제거 직급 생성
			levList.add(list.get(2));
			// 중복제거 부서 생성
			List<String> tmpDep = new ArrayList<String>();
			tmpDep.add(list.get(3));
			tmpDep.add(list.get(4));
			depList.add(tmpDep);
			// 중복제거 부문 생성
			List<String> tmpDiv = new ArrayList<String>();
			tmpDiv.add(list.get(5));
			tmpDiv.add(list.get(6));
			divList.add(tmpDiv);
		}

		// level, department, division의 정보 우선 모두 제거. 이건 체크박스 여부에 관계없이 엑셀로 업로드 될 때 항상 실행.
		staffService.deleteDistinctInfoByCno(cno);

		// 중복 제거된 list에서 lev등록
		levList.forEach(origin -> {
			Level level = new Level();
			level.setCno(cno);
			level.setContent(origin);
			level.setWriteId(staff.getWriteId());
			level.setUpdateId(staff.getUpdateId());
			levelService.register(level);
		});

		// 중복 제거된 list에서 Dep등록
		depList.forEach(data -> {
			Department department = new Department();
			department.setCno(cno);
			department.setDepartment1(data.get(0));
			department.setDepartment2(data.get(1));
			department.setWriteId(staff.getWriteId());
			department.setUpdateId(staff.getUpdateId());

			departmentService.register(department);
		});

		// 중복 제거된 list에서 Div등록
		divList.forEach(data -> {
			Division division = new Division();
			division.setCno(cno);
			division.setDivision1(data.get(0));
			division.setDivision2(data.get(1));
			division.setWriteId(staff.getWriteId());
			division.setUpdateId(staff.getUpdateId());

			divisionService.register(division);
		});

	}

}
