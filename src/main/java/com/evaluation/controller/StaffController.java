package com.evaluation.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import com.evaluation.domain.Admin;
import com.evaluation.domain.Company;
import com.evaluation.domain.Department;
import com.evaluation.domain.Division;
import com.evaluation.domain.Level;
import com.evaluation.domain.Staff;
import com.evaluation.function.AboutExcel;
import com.evaluation.function.RandomPassword;
import com.evaluation.persistence.StaffRepository;
import com.evaluation.service.CompanyService;
import com.evaluation.service.DepartmentService;
import com.evaluation.service.DistinctInfoService;
import com.evaluation.service.DivisionService;
import com.evaluation.service.LevelService;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	CompanyService companyService;

	StaffService staffService;

	TurnService turnService;

	DistinctInfoService distinctInfoService;

	LevelService levelService;

	DepartmentService departmentService;

	DivisionService divisionService;

	Relation360Service relation360Service;

	StaffRepository staffRepo;

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

	@GetMapping("/relation360/evaluated/{tno}")
	@ResponseBody
	public ResponseEntity<Optional<List<Staff>>> getStaffForEvaluated(@PathVariable("tno") long tno) {
		log.info("get All Staff List Exclude Evaluated....");

		long cno = turnService.get(tno).get().getCno();
		return new ResponseEntity<>(staffService.getEvaluatedList(cno, tno), HttpStatus.OK);
	}

	@GetMapping("/relation360/evaluator/{tno}/{sno}")
	@ResponseBody
	public ResponseEntity<Optional<List<Staff>>> getStaffForEvaluator(@PathVariable("tno") long tno,
			@PathVariable("sno") long sno) {
		log.info("get All Staff List....");

		long cno = turnService.get(tno).get().getCno();
		return new ResponseEntity<>(staffService.getEvaluatorList(cno, tno, sno), HttpStatus.OK);
	}

	@PostMapping("/upload")
	@ResponseBody
	public void xlUpload(long tno, Admin admin, MultipartFile uploadFile, Model model) {

		log.info("read file" + uploadFile);

		long cno = turnService.get(tno).get().getCno();

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

			// 메일 주소 이상시 건너뛰기
			if (!(list.get(1).contains("@") && list.get(1).contains("."))) {
				throw new IllegalArgumentException("메일 주소에 이상 문자가 포함되어있습니다.");
			}

			log.info("" + list);
			Staff row = new Staff();
			row.setCno(cno);
			row.setName(list.get(0));
			row.setEmail(list.get(1));
			row.setLevel(list.get(2));
			row.setDepartment1(list.get(3));
			row.setDepartment2(list.get(4));
			row.setDivision1(list.get(5));
			row.setDivision2(list.get(6));
			if (list.get(7) == null || list.get(7) == "N") {
				row.setPassword(RandomPassword.numberGen(6, 2));
			} else {
				row.setPassword(list.get(7));
			}
			row.setTelephone(list.get(8));
			row.setWriteId(admin.getWriteId());
			row.setUpdateId(admin.getUpdateId());

			// 새로 등록할지 업데이트할지 선택
			Optional<Staff> tmpStaff = staffService.readByEmail(list.get(1));
			if (tmpStaff.isPresent()) {
				row.setSno(tmpStaff.get().getSno());
				staffService.modify(row);
			} else {
				staffService.register(row);
			}

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
			level.setWriteId(admin.getWriteId());
			level.setUpdateId(admin.getUpdateId());
			levelService.register(level);
		});

		// 중복 제거된 list에서 Dep등록
		depList.forEach(data -> {
			Department department = new Department();
			department.setCno(cno);
			department.setDepartment1(data.get(0));
			department.setDepartment2(data.get(1));
			department.setWriteId(admin.getWriteId());
			department.setUpdateId(admin.getUpdateId());

			departmentService.register(department);
		});

		// 중복 제거된 list에서 Div등록
		divList.forEach(data -> {
			Division division = new Division();
			division.setCno(cno);
			division.setDivision1(data.get(0));
			division.setDivision2(data.get(1));
			division.setWriteId(admin.getWriteId());
			division.setUpdateId(admin.getUpdateId());

			divisionService.register(division);
		});

	}

	@PostMapping(value = "/xlDownload")
	@ResponseBody
	public void xlDown(long tno, HttpServletResponse response) {

		turnService.get(tno).ifPresent(origin -> {
			long cno = origin.getCno();

			String company = companyService.get(cno).map(Company::getName).orElse("etc");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
			String format_time = format.format(System.currentTimeMillis());

			String fileName = URLEncoder.encode(company + "_staff_" + format_time);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
			staffService.readBycno(cno).ifPresent(list -> {
				XSSFWorkbook workbook = new XSSFWorkbook();
				List<List<String>> xlList = new ArrayList<List<String>>();
				List<String> header = new ArrayList<String>();

				header.add("이름");
				header.add("이메일");
				header.add("직책");
				header.add("부문");
				header.add("부서");
				header.add("직군");
				header.add("계층");
				header.add("패스워드");
				header.add("전화번호");

				xlList.add(header);

				for (int i = 0; i < list.size(); i++) {
					List<String> tmpList = new ArrayList<String>();
					tmpList.add(list.get(i).getName());
					tmpList.add(list.get(i).getEmail());
					tmpList.add(list.get(i).getLevel());
					tmpList.add(list.get(i).getDepartment1());
					tmpList.add(list.get(i).getDepartment2());
					tmpList.add(list.get(i).getDivision1());
					tmpList.add(list.get(i).getDivision2());
					tmpList.add(list.get(i).getPassword());
					tmpList.add(list.get(i).getTelephone());
					xlList.add(tmpList);
				}

				workbook = AboutExcel.writeExcel(xlList);

				try {
					workbook.write(response.getOutputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});
	}
}
