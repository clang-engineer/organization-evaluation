package com.evaluation.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Staff;
import com.evaluation.service.DistinctInfoService;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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

	@Setter(onMethod_ = { @Autowired })
	DistinctInfoService distinctInfoService;

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

		rttr.addFlashAttribute("msg", "success");
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

		rttr.addFlashAttribute("msg", "success");

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
		rttr.addFlashAttribute("msg", "success");
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
	public void csvUpload(long tno, Staff staff, Boolean deleteList, MultipartFile uploadFile, Model model) {

		log.info("read file" + uploadFile);
		log.info("" + deleteList);

		long cno = turnService.get(tno).get().getCno();
		if (deleteList == true) {
			staffService.deleteByCno(cno);
		}

		int iteration = 0;
		List<List<String>> allData = readExcel(uploadFile);

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
			row.setPassword(list.get(8));
			row.setTelephone(list.get(9));
			row.setWriteId(staff.getWriteId());
			row.setUpdateId(staff.getUpdateId());

			staffService.register(row);
		}
	}

	public List<List<String>> readExcel(MultipartFile uploadFile) {
		List<List<String>> ret = new ArrayList<List<String>>();

		try {

			InputStream is = uploadFile.getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(is); // 2007 이후 버전(xlsx파일)

			int rowindex = 0;
			int columnindex = 0;
			// 시트 수 (첫번째에만 존재하므로 0을 준다)
			// 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
			XSSFSheet sheet = workbook.getSheetAt(0);
			// 행의 수
			int rows = sheet.getPhysicalNumberOfRows();
			for (rowindex = 0; rowindex < rows; rowindex++) {
				// 행을읽는다
				XSSFRow row = sheet.getRow(rowindex);
				if (row != null) {
					List<String> tmpList = new ArrayList<String>();
					// 셀의 수
					int cells = row.getPhysicalNumberOfCells();
					for (columnindex = 0; columnindex <= cells; columnindex++) {
						// 셀값을 읽는다
						XSSFCell cell = row.getCell(columnindex);
						String value = "";
						// 셀이 빈값일경우를 위한 널체크
						if (cell == null) {
							value = "1";
						} else {
							// 타입별로 내용 읽기
							switch (cell.getCellType()) {
							case XSSFCell.CELL_TYPE_FORMULA:
								value = cell.getCellFormula();
								break;
							case XSSFCell.CELL_TYPE_NUMERIC:
								value = cell.getNumericCellValue() + "";
								break;
							case XSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue() + "";
								break;
							case XSSFCell.CELL_TYPE_BLANK:
								// value = cell.getBooleanCellValue() + "";
								value = "";
								break;
							case XSSFCell.CELL_TYPE_ERROR:
								value = cell.getErrorCellValue() + "";
								break;
							}
						}
						tmpList.add(value);
					}
					ret.add(tmpList);
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
