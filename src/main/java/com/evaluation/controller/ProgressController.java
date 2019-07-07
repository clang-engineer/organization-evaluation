package com.evaluation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.evaluation.domain.Company;
import com.evaluation.function.AboutExcel;
import com.evaluation.service.CompanyService;
import com.evaluation.service.MboService;
import com.evaluation.service.QuestionService;
import com.evaluation.service.Relation360Service;
import com.evaluation.service.RelationMboService;
import com.evaluation.service.TurnService;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/progress/*")
@AllArgsConstructor
public class ProgressController {

    Relation360Service relation360Service;

    RelationMboService relationMboService;

    TurnService turnService;

    CompanyService companyService;

    QuestionService questionService;

    MboService mboService;

    @GetMapping(value = { "/survey", "/mbo" })
    public void survey(long tno, Model model, HttpServletRequest request) {
        String whatYouCall = request.getServletPath();

        if (whatYouCall.equals("/progress/survey")) {
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
        } else if (whatYouCall.equals("/progress/mbo")) {
            relationMboService.progressOfSurevey(tno).ifPresent(origin -> {
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
        }

        model.addAttribute("tno", tno);
    }

    // 평가자의 피평가자 리스트 얻어오는 컨트롤러 중복 제거 위해, 합침. 다형성 이용해서 중복 더 줄이고 싶으나, 복잡성 증가해서 스탑.
    @GetMapping(value = { "/survey/evaluatedList", "/mbo/evaluatedList" })
    public void mboEvaluated(long tno, long sno, Model model, HttpServletRequest request) {
        String whatYouCall = request.getServletPath();

        if (whatYouCall.equals("/progress/survey/evaluatedList")) {
            relation360Service.findByEvaluator(sno, tno).ifPresent(origin -> {
                model.addAttribute("evaluatedList", origin);
            });
        } else if (whatYouCall.equals("/progress/mbo/evaluatedList")) {
            relationMboService.findByEvaluator(sno, tno).ifPresent(origin -> {
                model.addAttribute("evaluatedList", origin);
            });
        }
        model.addAttribute("tno", tno);
    }

    // 평가자의 피평가자 서베이 저장 상태 바꿀 수 있도록하는 REST
    @PutMapping(value = { "/survey/evaluatedList", "/mbo/evaluatedList" })
    public ResponseEntity<HttpStatus> mboEvaluatedFinishChange(long rno, String finish, HttpServletRequest request) {
        String whatYouCall = request.getServletPath();

        if (whatYouCall.equals("/progress/survey/evaluatedList")) {
            relation360Service.read(rno).ifPresent(origin -> {
                origin.setFinish(finish);
                relation360Service.modify(origin);
            });
        } else if (whatYouCall.equals("/progress/mbo/evaluatedList")) {
            relationMboService.read(rno).ifPresent(origin -> {
                origin.setFinish(finish);
                relationMboService.modify(origin);
            });
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/survey")
    @ResponseBody
    public void surveyXlDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.read(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_surveyProgress _" + format_time, "UTF-8");
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

                int completeCount = 0;
                int totalCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(list.get(i).get(0));
                    tmpList.add(list.get(i).get(1));
                    tmpList.add(list.get(i).get(2));
                    tmpList.add(list.get(i).get(3));
                    tmpList.add(list.get(i).get(4));
                    tmpList.add(list.get(i).get(5));
                    tmpList.add(list.get(i).get(6));
                    Double tmpValue = Double.parseDouble(list.get(i).get(7));
                    tmpList.add(String.format("%.0f", tmpValue * 100) + "%");
                    xlList.add(tmpList);

                    completeCount += Integer.parseInt(list.get(i).get(5));
                    totalCount += Integer.parseInt(list.get(i).get(6));
                }

                // 엑셀 바닥글 설정
                List<String> footer = new ArrayList<String>();
                String[] space = { "-", "-", "-", "-" };
                footer.addAll(Arrays.asList(space));
                footer.add("Total");
                footer.add(Integer.toString(completeCount));
                footer.add(Integer.toString(totalCount));
                footer.add(Integer.toString((completeCount / totalCount) * 100) + '%');
                xlList.add(footer);

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

    @PostMapping("/survey/result")
    @ResponseBody
    public void surveyResultDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.read(cno).map(Company::getName).orElse("etc");

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
                header.add("입력일");

                // question중 문항 idx 중복 제거 최대값 구해서 해당 값들을 열 머릿글로 구성한다. 순서대로 입력하려고 linkedHashSet사용
                Set<String> answerKeySet = new LinkedHashSet<String>();
                questionService.findByTno(tno).ifPresent(qlist -> {
                    for (int i = 0; i < qlist.size(); i++) {
                        answerKeySet.add('q' + Integer.toString(qlist.get(i).getIdx()));
                    }
                });
                header.addAll(answerKeySet);

                // comment는 size로 키 추정.
                Set<String> commentKeySet = new LinkedHashSet<String>();
                turnService.get(tno).ifPresent(turn -> {
                    for (int i = 0; i < turn.getComments().size(); i++) {
                        commentKeySet.add("c" + (i + 1));
                    }
                });
                header.addAll(commentKeySet);

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
                    // 입력시간은 N가 아닌 것들만 입력 해준다!
                    if (list.get(i).getFinish().equals("N")) {
                        tmpList.add(null);
                    } else {
                        tmpList.add("" + list.get(i).getUpdateDate());
                    }
                    // answer를 위에서 만든 key로 for문 돌린다.
                    for (String key : answerKeySet) {
                        if (list.get(i).getAnswers().get(key) == null) {
                            tmpList.add(null);
                        } else {
                            tmpList.add("" + list.get(i).getAnswers().get(key));
                        }
                    }
                    // comment를 위에서 만든 key로 for문 돌린다.
                    for (String key : commentKeySet) {
                        if (list.get(i).getComments().get(key) == null) {
                            tmpList.add(null);
                        } else {
                            tmpList.add("" + list.get(i).getComments().get(key));
                        }
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

    // mbo의 평가 progress 다운받기
    @PostMapping("/mbo")
    @ResponseBody
    public void seeXlDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.read(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_mboSeeProgress_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            relationMboService.progressOfSurevey(tno).ifPresent(list -> {
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

                int completeCount = 0;
                int totalCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(list.get(i).get(0));
                    tmpList.add(list.get(i).get(1));
                    tmpList.add(list.get(i).get(2));
                    tmpList.add(list.get(i).get(3));
                    tmpList.add(list.get(i).get(4));
                    tmpList.add(list.get(i).get(5));
                    tmpList.add(list.get(i).get(6));
                    Double tmpValue = Double.parseDouble(list.get(i).get(7));
                    tmpList.add(String.format("%.0f", tmpValue * 100) + "%");
                    xlList.add(tmpList);

                    completeCount += Integer.parseInt(list.get(i).get(5));
                    totalCount += Integer.parseInt(list.get(i).get(6));
                }

                // 엑셀 바닥글 설정
                List<String> footer = new ArrayList<String>();
                String[] space = { "-", "-", "-", "-" };
                footer.addAll(Arrays.asList(space));
                footer.add("Total");
                footer.add(Integer.toString(completeCount));
                footer.add(Integer.toString(totalCount));
                footer.add(Integer.toString((completeCount / totalCount) * 100) + '%');
                xlList.add(footer);

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

    @PostMapping("/mbo/result")
    @ResponseBody
    public void mboResultDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.read(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_mboSeeResult_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            relationMboService.findAllbyTno(tno).ifPresent(list -> {
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
                header.add("입력일");

                // set으로 중복제거
                Set<String> answerKeySet = new LinkedHashSet<String>();
                Set<String> commentKeySet = new LinkedHashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    answerKeySet.addAll(list.get(i).getAnswers().keySet());
                    commentKeySet.addAll(list.get(i).getComments().keySet());
                }

                // list로 변환 후 정렬
                List<String> answerKeyList = new ArrayList<String>(answerKeySet);
                questionSort(answerKeyList);

                // sum을 표시해주는 열 따로 생성
                List<String> ratioHeaderList = new LinkedList<String>();
                List<String> valueHeaderList = new LinkedList<String>();
                List<String> multipleHeaderList = new LinkedList<String>();
                for (int i = 0; i < answerKeyList.size(); i++) {
                    String ratioString = "r-" + answerKeyList.get(i);
                    String valueString = "v-" + answerKeyList.get(i);
                    String multipleString = "r*v-" + answerKeyList.get(i);
                    ratioHeaderList.add(i, ratioString);
                    valueHeaderList.add(i, valueString);
                    multipleHeaderList.add(i, multipleString);
                }
                header.addAll(ratioHeaderList);
                header.addAll(valueHeaderList);
                header.addAll(multipleHeaderList);
                header.addAll(commentKeySet);

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
                    // 입력시간은 N가 아닌 것들만 입력 해준다!
                    if (list.get(i).getFinish().equals("N")) {
                        tmpList.add(null);
                    } else {
                        tmpList.add("" + list.get(i).getUpdateDate());
                    }

                    // ratio추출해서 더하고
                    for (String key : answerKeyList) {
                        if (list.get(i).getAnswers().get(key) == null) {
                            tmpList.add(null);
                        } else {
                            tmpList.add("" + list.get(i).getAnswers().get(key).getRatio());
                        }
                    }

                    // value 추출해서 더하고
                    for (String key : answerKeyList) {
                        if (list.get(i).getAnswers().get(key) == null) {
                            tmpList.add(null);
                        } else {
                            tmpList.add("" + list.get(i).getAnswers().get(key).getValue());
                        }
                    }

                    // ratio*value를 더한다.
                    for (String key : answerKeyList) {
                        if (list.get(i).getAnswers().get(key) == null) {
                            tmpList.add(null);
                        } else {
                            tmpList.add("" + list.get(i).getAnswers().get(key).getRatio()
                                    * list.get(i).getAnswers().get(key).getValue());
                        }
                    }

                    // comment를 위에서 만든 key로 for문 돌린다.
                    for (String key : commentKeySet) {
                        if (list.get(i).getComments().get(key) == null) {
                            tmpList.add(null);
                        } else {
                            tmpList.add("" + list.get(i).getComments().get(key));
                        }
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

    // 커스텀 sort를 위한
    public void questionSort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.equals("weight") || s2.equals("weight")) {
                    return 1;
                }

                if (Integer.parseInt(s1.substring(1)) < Integer.parseInt(s2.substring(1))) {
                    return -1;
                } else if (Integer.parseInt(s1.substring(1)) > Integer.parseInt(s2.substring(1))) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @GetMapping("/mbo/plan")
    public void progressOfPlan(long tno, Model model) {
        relationMboService.progressOfPlan(tno).ifPresent(list -> {
            model.addAttribute("progress", list);
            double total = 0;
            for (List<String> tmpList : list) {

                total += (tmpList.get(6) != null) ? Double.parseDouble(tmpList.get(6)) : 0;
            }
            model.addAttribute("count", list.size());
            model.addAttribute("total", total);
        });
        model.addAttribute("tno", tno);
    }

    @GetMapping("/mbo/objectList")
    public void objectList(long tno, long sno, Model model) {
        model.addAttribute("tno", tno);
        mboService.listByTnoSno(tno, sno).ifPresent(list -> {
            model.addAttribute("objectList", list);
        });
    }

    @PostMapping("/mbo/plan")
    @ResponseBody
    public void planXlDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.read(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_mboPlanProgress _" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            relationMboService.progressOfPlan(tno).ifPresent(list -> {
                List<List<String>> xlList = new ArrayList<List<String>>();
                List<String> header = new ArrayList<String>();

                header.add("이름");
                header.add("이메일");
                header.add("직책");
                header.add("부문");
                header.add("부서");
                header.add("완료");

                xlList.add(header);

                Double completeCount = 0.0;
                int totalCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(list.get(i).get(1));
                    tmpList.add(list.get(i).get(2));
                    tmpList.add(list.get(i).get(3));
                    tmpList.add(list.get(i).get(4));
                    tmpList.add(list.get(i).get(5));
                    if (list.get(i).get(6) != null) {
                        Double tmpValue = Double.parseDouble(list.get(i).get(6));
                        tmpList.add(String.format("%.0f", tmpValue * 100) + "%");
                        completeCount += tmpValue;
                    } else {
                        tmpList.add("");
                    }
                    xlList.add(tmpList);
                }

                totalCount = list.size();
                // 엑셀 바닥글 설정
                List<String> footer = new ArrayList<String>();
                String[] space = { "-", "-", "-", "-" };
                footer.addAll(Arrays.asList(space));
                footer.add("Total");
                footer.add(String.format("%.0f", completeCount / totalCount * 100) + "%");
                xlList.add(footer);

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

    @PostMapping("/mbo/plan/result")
    @ResponseBody
    public void mboPlanResultDownload(long tno, HttpServletResponse response) {

        turnService.get(tno).ifPresent(origin -> {
            long cno = origin.getCno();
            String company = companyService.read(cno).map(Company::getName).orElse("etc");

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd//HHmmss");
            String format_time = format.format(System.currentTimeMillis());

            String fileName = "default";
            try {
                fileName = URLEncoder.encode(company + "_mboPlanResult_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            mboService.listByTno(tno).ifPresent(list -> {
                List<List<String>> xlList = new ArrayList<List<String>>();
                List<String> header = new ArrayList<String>();

                header.add("#");
                header.add("이름");
                header.add("이메일");
                header.add("직책");
                header.add("부문");
                header.add("부서");
                header.add("finish");
                header.add("목표");
                header.add("프로세스");
                header.add("비율");

                xlList.add(header);
                int index = 0;
                for (List<String> objectList : list) {
                    List<String> tmpList = new ArrayList<String>();
                    tmpList.add(Integer.toString(index + 1));
                    tmpList.add(objectList.get(0));
                    tmpList.add(objectList.get(1));
                    tmpList.add(objectList.get(2));
                    tmpList.add(objectList.get(3));
                    tmpList.add(objectList.get(4));
                    tmpList.add(objectList.get(5));
                    tmpList.add(objectList.get(6));
                    tmpList.add(objectList.get(7));
                    tmpList.add(String.format("%.0f", Double.parseDouble(objectList.get(8)) * 100) + "%");
                    xlList.add(tmpList);
                    index++;
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