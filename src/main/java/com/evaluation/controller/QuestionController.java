package com.evaluation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.evaluation.domain.Company;
import com.evaluation.domain.Question;
import com.evaluation.function.AboutExcel;
import com.evaluation.service.CompanyService;
import com.evaluation.service.QuestionService;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.evaluation.vo.PageMaker;
import com.evaluation.vo.PageVO;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/question/*")
@Slf4j
@AllArgsConstructor
public class QuestionController {

    QuestionService questionService;

    StaffService staffService;

    TurnService turnService;

    CompanyService companyService;

    @GetMapping("/register")
    public void register(long tno, PageVO vo, Model model) {
        log.info("register get by " + tno + vo);

        model.addAttribute("tno", tno);

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("distinctInfo", questionService.getDistinctQuestionInfo(cno, tno));
        });
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

        model.addAttribute("tno", tno);

        questionService.read(qno).ifPresent(origin -> {
            model.addAttribute("question", origin);
        });

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("distinctInfo", questionService.getDistinctQuestionInfo(cno, tno));
        });
    }

    @GetMapping("/modify")
    public void modify(long qno, long tno, PageVO vo, Model model) {
        log.info("modify by " + tno + vo);

        model.addAttribute("tno", tno);

        questionService.read(qno).ifPresent(origin -> {
            model.addAttribute("question", origin);
        });

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            model.addAttribute("distinctInfo", questionService.getDistinctQuestionInfo(cno, tno));
        });
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

    @PostMapping("/xlUpload")
    @ResponseBody
    public void xlUpload(Question question, Boolean deleteList, MultipartFile uploadFile, Model model) {

        log.info("read file" + uploadFile);
        log.info("" + deleteList);

        if (deleteList == true) {
            questionService.deleteByTno(question.getTno());
        }

        int iteration = 0;
        List<List<String>> allData = AboutExcel.readExcel(uploadFile);

        for (List<String> list : allData) {
            if (iteration == 0) {
                iteration++;
                continue;
            }

            Question row = new Question();
            row.setTno(question.getTno());
            row.setCategory(list.get(0));
            row.setIdx(Integer.parseInt(list.get(1)));
            row.setItem(list.get(2));
            row.setDivision1(list.get(3));
            row.setDivision2(list.get(4));
            row.setRatio(Double.parseDouble(list.get(5)));
            row.setWriteId(question.getWriteId());
            row.setUpdateId(question.getUpdateId());

            questionService.register(row);
        }
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

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_question_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            questionService.findAllByTno(tno).ifPresent(list -> {
                List<List<String>> xlList = new ArrayList<List<String>>();
                List<String> header = new ArrayList<String>();

                header.add("요소");
                header.add("번호");
                header.add("항목");
                header.add("직군");
                header.add("계층");
                header.add("비중");

                xlList.add(header);

                for (int i = 0; i < list.size(); i++) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(list.get(i).getCategory());
                    tmpList.add(Integer.toString(list.get(i).getIdx()));
                    tmpList.add(list.get(i).getItem());
                    tmpList.add(list.get(i).getDivision1());
                    tmpList.add(list.get(i).getDivision2());
                    tmpList.add(Double.toString(list.get(i).getRatio()));
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