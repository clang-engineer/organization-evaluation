package com.evaluation.controller;

import java.io.UnsupportedEncodingException;
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
import com.evaluation.domain.Turn;
import com.evaluation.function.AboutExcel;
import com.evaluation.function.RandomPassword;
import com.evaluation.service.CompanyService;
import com.evaluation.service.DepartmentService;
import com.evaluation.service.DivisionService;
import com.evaluation.service.LevelService;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

/**
 * <code>StaffController</code>객체는 직원 정보를 관리한다.
 */
@Controller
@RequestMapping("/staff/*")
@Slf4j
@AllArgsConstructor
@Transactional
public class StaffController {

	CompanyService companyService;

	TurnService turnService;

	StaffService staffService;

	LevelService levelService;

	DepartmentService departmentService;

	DivisionService divisionService;

	/**
	 * 직원 등록 페이지를 읽어온다.
	 * 
	 * @param tno   회차 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/register")
	public void register(long tno, PageVO vo, Model model) {
		log.info("controller : staff register get by " + tno + vo);

		turnService.read(tno).ifPresent(turn -> {
			long cno = turn.getCno();
			model.addAttribute("distinctInfo", staffService.getDistinctInfo(cno, tno));
		});

		model.addAttribute("tno", tno);
	}

	/**
	 * 직원을 등록한다.
	 * 
	 * @param tno   회차 id
	 * @param staff 직원정보
	 * @param rttr  재전송 정보
	 * @return 직원 목록 페이지
	 */
	@PostMapping("/register")
	public String register(long tno, Staff staff, RedirectAttributes rttr) {
		log.info("controller : staff register post by " + tno);

		long cno = turnService.read(tno).map(Turn::getCno).orElse(0L);
		staff.setCno(cno);
		staffService.register(staff);

		rttr.addFlashAttribute("msg", "register");
		rttr.addAttribute("tno", tno);
		return "redirect:/staff/list";
	}

	/**
	 * 직원 정보를 읽어온다.
	 * 
	 * @param tno   회차 id
	 * @param sno   직원 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/view")
	public void read(long tno, long sno, PageVO vo, Model model) {
		log.info("controller : staff read by " + tno + vo);

		Staff staff = staffService.read(sno).orElse(null);

		model.addAttribute("tno", tno);
		model.addAttribute("staff", staff);
	}

	/**
	 * 직원 정보 수젱 페이지를 읽어온다.
	 * 
	 * @param tno   회차 id
	 * @param sno   직원 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/modify")
	public void modify(long tno, long sno, PageVO vo, Model model) {
		log.info("controller : staff modify by " + tno + vo);

		model.addAttribute("tno", tno);

		turnService.read(tno).ifPresent(turn -> {
			long cno = turn.getCno();
			model.addAttribute("distinctInfo", staffService.getDistinctInfo(cno, tno));
		});

		staffService.read(sno).ifPresent(staff -> {
			model.addAttribute("staff", staff);
		});
	}

	/**
	 * 직원 정보를 수정한다.
	 * 
	 * @param tno   회차 id
	 * @param staff 직원 정보
	 * @param vo    페이지 정보
	 * @param rttr  재전송 정보
	 * @return 직원 정보 페이지
	 */
	@PostMapping("/modify")
	public String modify(long tno, Staff staff, PageVO vo, RedirectAttributes rttr) {
		log.info("controller : staff modify post by " + staff.getName());

		long cno = turnService.read(tno).map(Turn::getCno).orElse(null);
		staff.setCno(cno);

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

	/**
	 * 직원 정보를 삭제한다.
	 * 
	 * @param tno  회차 id
	 * @param sno  직원 id
	 * @param vo   페이지 정보
	 * @param rttr 재전송 정보
	 * @return 직원 목록 페이지
	 */
	@PostMapping("/remove")
	public String remove(long tno, long sno, PageVO vo, RedirectAttributes rttr) {
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

	/**
	 * 회차의 직원 목록을 읽는다.
	 * 
	 * @param tno   회차 id
	 * @param vo    페이지 정보
	 * @param model 화면 전달 정보
	 */
	@GetMapping("/list")
	public void readList(long tno, PageVO vo, Model model) {
		log.info("controller : staff list by " + tno + vo);

		model.addAttribute("tno", tno);

		long cno = turnService.read(tno).map(Turn::getCno).orElse(null);

		Page<Staff> result = staffService.getList(cno, vo);
		model.addAttribute("result", new PageMaker<>(result));
	}

	/**
	 * 직원 정보를 xl업로드 한다.
	 * 
	 * @param tno        회차 id
	 * @param admin      단순히 기록일, 수정일을 받아오기 위해 사용.(parameter 줄이려고) 추후 수정하자.
	 * @param uploadFile 업로드 파일
	 * @param model      화면 전달 정보
	 */
	@PostMapping("/xlUpload")
	@ResponseBody
	public void xlUpload(long tno, Admin admin, MultipartFile uploadFile, Model model) {

		log.info("read file" + uploadFile);

		long cno = turnService.read(tno).map(Turn::getCno).orElse(null);

		int iteration = 0;
		List<List<String>> allData = AboutExcel.readExcel(uploadFile);

		// 중복 제거를 위한 부서! 엑셀로 등록하는 부서 중에서만 생성. (부서만 tno참조이므로 (cno(X)))
		Set<List<String>> depList = new HashSet<List<String>>();

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

			// 중복제거 부서 추가해놓음.
			List<String> tmpDep = new ArrayList<String>();
			tmpDep.add(list.get(3));
			tmpDep.add(list.get(4));
			depList.add(tmpDep);

			// 직원 정보 생성 및 등록
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
			Optional<Staff> tmpStaff = staffService.findByEmail(list.get(1));
			if (tmpStaff.isPresent()) {
				row.setSno(tmpStaff.get().getSno());
				staffService.modify(row);
			} else {
				staffService.register(row);
			}

		}

		// 중복 제거 직급, 직군-계층, Set두개값 문자 조합 중복 체크함
		Set<String> levList = new TreeSet<String>();
		Set<List<String>> divList = new HashSet<List<String>>();

		// 회사에 속한 전 직원 정보 중복제거 하기
		staffService.findByCno(cno).ifPresent(origin -> {
			origin.forEach(staff -> {
				// 중복제거 직급 생성
				levList.add(staff.getLevel());
				// 중복제거 직군, 계층 생성
				List<String> tmpDiv = new ArrayList<String>();
				tmpDiv.add(staff.getDivision1());
				tmpDiv.add(staff.getDivision2());
				divList.add(tmpDiv);
			});
		});

		// level, department, division의 정보 우선 모두 제거. 이건 체크박스 여부에 관계없이 엑셀로 업로드 될 때 항상 실행.
		staffService.deleteDistinctInfoByTnoCno(cno, tno);

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
			department.setTno(tno);
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

	/**
	 * 회차에 속원 직원 목록을 xl 다운로드 한다.
	 * 
	 * @param tno      회차 id
	 * @param response 응답 정보 객체
	 */
	@PostMapping(value = "/xlDownload")
	@ResponseBody
	public void xlDown(long tno, HttpServletResponse response) {

		turnService.read(tno).ifPresent(origin -> {
			long cno = origin.getCno();

			String company = companyService.read(cno).map(Company::getName).orElse("etc");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
			String format_time = format.format(System.currentTimeMillis());

			String fileName = "default";
			try {
				fileName = URLEncoder.encode(company + "_staff_" + format_time, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
			staffService.findByCno(cno).ifPresent(list -> {

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

				try {
					XSSFWorkbook workbook = AboutExcel.writeExcel(xlList);
					workbook.write(response.getOutputStream());
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
		});
	}
}
