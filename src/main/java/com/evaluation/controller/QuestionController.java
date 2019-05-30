package com.evaluation.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Question;
import com.evaluation.service.DistinctInfoService;
import com.evaluation.service.QuestionService;
import com.evaluation.service.StaffService;
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
@RequestMapping("/question/*")
@Slf4j
public class QuestionController {

    @Setter(onMethod_ = { @Autowired })
    QuestionService questionService;

    @Setter(onMethod_ = { @Autowired })
    StaffService staffService;

    @Setter(onMethod_ = { @Autowired })
    DistinctInfoService distinctInfoservice;

    @GetMapping("/register")
    public void register(long tno, PageVO vo, Model model) {
        log.info("register get by " + tno + vo);

        model.addAttribute("tno", tno);
        model.addAttribute("distinctInfo", distinctInfoservice.getDistinctQuestionInfo(tno));
    }

    @PostMapping("/register")
    public String register(Question question, long tno, RedirectAttributes rttr) {
        log.info("register post by " + question);

        question.setTno(tno);
        questionService.register(question);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", tno);
        return "redirect:/question/list";
    }

    @GetMapping("/view")
    public void read(long qno, long tno, PageVO vo, Model model) {
        log.info("view by " + tno + vo);

        Optional<Question> question = questionService.read(qno);

        model.addAttribute("tno", tno);
        model.addAttribute("distinctInfo", distinctInfoservice.getDistinctQuestionInfo(tno));

        Question result = question.get();
        model.addAttribute("question", result);
    }

    @GetMapping("/modify")
    public void modify(long qno, long tno, PageVO vo, Model model) {
        log.info("modify by " + tno + vo);

        Optional<Question> question = questionService.read(qno);

        model.addAttribute("tno", tno);
        model.addAttribute("distinctInfo", distinctInfoservice.getDistinctQuestionInfo(tno));

        Question result = question.get();
        model.addAttribute("question", result);
    }

    @PostMapping("/modify")
    public String modify(Question question, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("modify" + question);

        questionService.modify(question);
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("qno", question.getQno());
        rttr.addFlashAttribute("msg", "modify");
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/question/view";
    }

    @PostMapping("/remove")
    public String remove(long qno, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + qno);

        questionService.remove(qno);

        rttr.addAttribute("tno", tno);
        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/question/list";
    }

    @GetMapping("/list")
    public void readList(long tno, PageVO vo, Model model) {
        log.info("question list by " + tno + vo);

        model.addAttribute("tno", tno);

        Page<Question> result = questionService.getList(tno, vo);
        model.addAttribute("result", new PageMaker<>(result));
    }

    @PostMapping("/upload")
    @ResponseBody
    public void csvUpload(Question question, Boolean deleteList, MultipartFile uploadFile, Model model) {

        log.info("read file" + uploadFile);
        log.info("" + deleteList);

        if (deleteList == true) {
            questionService.deleteByTno(question.getTno());
        }

        int iteration = 0;
        List<List<String>> allData = readExcel(uploadFile);

        for (List<String> newLine : allData) {
            if (iteration == 0) {
                iteration++;
                continue;
            }
            List<String> list = newLine;
            // log.info("" + list);
            Question row = new Question();
            row.setTno(question.getTno());
            row.setIdx(list.get(0));
            row.setCategory(list.get(1));
            row.setItem(list.get(2));
            row.setDivision1(list.get(3));
            row.setDivision2(list.get(4));
            row.setWriteId(question.getWriteId());
            row.setUpdateId(question.getUpdateId());

            questionService.register(row);
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
                            continue;
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
                                value = cell.getBooleanCellValue() + "";
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
