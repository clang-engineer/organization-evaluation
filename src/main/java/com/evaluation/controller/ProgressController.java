package com.evaluation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.evaluation.domain.Company;
import com.evaluation.function.AboutExcel;
import com.evaluation.service.CompanyService;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.TurnService;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/progress/**")
@AllArgsConstructor
@Slf4j
public class ProgressController {

    Relation360Service relation360Service;

    TurnService turnService;

    CompanyService companyService;

    @GetMapping("/survey")
    public void survey(long tno, Model model) {

        relation360Service.progressOfSurevey(tno).ifPresent(origin -> {
            model.addAttribute("progress", origin);

            // 총 개수 구하기
            int completeCount = 0;
            int totalCount = 0;
            for (int i = 0; i < origin.size(); i++) {
                completeCount += Integer.parseInt(origin.get(i).get(5));
                totalCount += Integer.parseInt(origin.get(i).get(6));
            }

            model.addAttribute("completeCount", completeCount);
            model.addAttribute("totalCount", totalCount);
        });

        model.addAttribute("tno", tno);
    }

    @PostMapping("/survey")
    @ResponseBody
    public void surveyXlDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.get(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_surveyProgress_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            relation360Service.progressOfSurevey(tno).ifPresent(list -> {
                List<List<String>> xlList = new ArrayList<List<String>>();
                List<String> header = new ArrayList<String>();

                header.add("이름");
                header.add("이메일");
                header.add("직책");
                header.add("부문");
                header.add("부서");
                header.add("완료");
                header.add("총");
                header.add("비율");

                xlList.add(header);

                for (int i = 0; i < list.size(); i++) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(list.get(i).get(0));
                    tmpList.add(list.get(i).get(1));
                    tmpList.add(list.get(i).get(2));
                    tmpList.add(list.get(i).get(3));
                    tmpList.add(list.get(i).get(4));
                    tmpList.add(list.get(i).get(5));
                    tmpList.add(list.get(i).get(6));
                    tmpList.add(list.get(i).get(7));
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

    @GetMapping("/survey/result")
    @ResponseBody
    public void surveyResultDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.get(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_suveyResult_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            relation360Service.findAllbyTno(tno).ifPresent(list -> {
                List<List<String>> xlList = new ArrayList<List<String>>();
                List<String> header = new ArrayList<String>();

                header.add("이름");
                header.add("이메일");
                header.add("직책");
                header.add("부문");
                header.add("부서");
                header.add("직군");
                header.add("계층");
                header.add("관계");
                header.add("평가자");
                header.add("임시저장");

                // 키에서 header 추출하기
                Set<String> answerKeySet = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    // answer를 위한
                    Set<Map.Entry<String, Integer>> entries = list.get(i).getAnswers().entrySet();
                    for (Map.Entry<String, Integer> entry : entries) {
                        log.info("" + entry.getKey());
                        answerKeySet.add(entry.getKey());
                    }
                }
                List<String> answerKeyList = new ArrayList<String>(answerKeySet);

                // q뺀 문자열 정렬
                questionSort(answerKeyList);
                header.addAll(answerKeyList);

                // 키에서 header 추출하기
                Set<String> commentKeySet = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    // answer를 위한
                    Set<Map.Entry<String, String>> entries = list.get(i).getComments().entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        log.info("" + entry.getKey());
                        answerKeySet.add(entry.getKey());
                    }
                }
                List<String> commentKeyList = new ArrayList<String>(commentKeySet);

                // q뺀 문자열 정렬
                questionSort(commentKeyList);
                header.addAll(commentKeyList);

                xlList.add(header);

                for (int i = 0; i < list.size(); i++) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(list.get(i).getEvaluated().getName());
                    tmpList.add(list.get(i).getEvaluated().getEmail());
                    tmpList.add(list.get(i).getEvaluated().getLevel());
                    tmpList.add(list.get(i).getEvaluated().getDepartment1());
                    tmpList.add(list.get(i).getEvaluated().getDepartment2());
                    tmpList.add(list.get(i).getEvaluated().getDivision1());
                    tmpList.add(list.get(i).getEvaluated().getDivision2());
                    tmpList.add(list.get(i).getRelation());
                    tmpList.add(list.get(i).getEvaluator().getEmail());
                    tmpList.add(list.get(i).getFinish());
                    for (String key : answerKeyList) {
                        tmpList.add("" + list.get(i).getAnswers().get(key));
                    }
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

    public void questionSort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (Integer.parseInt(s1.substring(1)) < Integer.parseInt(s2.substring(1))) {
                    return -1;
                } else if (Integer.parseInt(s1.substring(1)) > Integer.parseInt(s2.substring(1))) {
                    return 1;
                }
                return 0;
            }
        });
    }
}