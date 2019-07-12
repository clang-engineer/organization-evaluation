package com.evaluation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.evaluation.domain.Company;
import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Staff;
import com.evaluation.domain.Turn;
import com.evaluation.function.AboutExcel;
import com.evaluation.service.CompanyService;
import com.evaluation.service.RelationMboService;
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

/**
 * <code>RelationMboController</code> 객체는 mbo 관계정보를 관리한다.
 */
@Controller
@RequestMapping("/relationMbo/*")
@Slf4j
@AllArgsConstructor
@Transactional
public class RelationMboController {

    RelationMboService relationMboService;

    StaffService staffService;

    TurnService turnService;

    CompanyService companyService;

    /**
     * 관계를 등록한다.
     * 
     * @param relationMbo mbo 관계 정보
     * @param vo          페이지 정보
     * @param rttr        재전송 정보
     * @return 관계 목록 페이지
     */
    @PostMapping("/register")
    public String register(RelationMbo relationMbo, PageVO vo, RedirectAttributes rttr) {
        log.info("register " + relationMbo.getEvaluated().getSno());

        relationMboService.register(relationMbo);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", relationMbo.getTno());
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relationMbo/list";
    }

    /**
     * 관계를 삭제한다.
     * 
     * @param tno  회차 id
     * @param rno  관계 id
     * @param vo   페이지 정보
     * @param rttr 재전송 정보
     * @return 관계 목록 페이지
     */
    @PostMapping("/remove")
    public String remove(long tno, long rno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + tno + rno);

        relationMboService.remove(rno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relationMbo/list";
    }

    /**
     * 관계 목록을 읽는다.
     * 
     * @param tno   회차 id
     * @param vo    페이지 정보
     * @param model 화면 전달 정보
     */
    @GetMapping("/list")
    public void getList(long tno, PageVO vo, Model model) {
        log.info("getList by " + tno);

        model.addAttribute("tno", tno);

        // 우선 중복 제거한 피평가자 paging처리해서 구함
        Page<Staff> result = relationMboService.getDistinctEvaluatedList(tno, vo);
        model.addAttribute("result", new PageMaker<>(result));

        // 화면에 표시되는 피평가자들의 관계별 relationTable만들기
        List<RelationMbo> relationMe = new ArrayList<>();
        List<RelationMbo> relation1 = new ArrayList<>();
        List<RelationMbo> relation2 = new ArrayList<>();
        List<RelationMbo> relation3 = new ArrayList<>();
        result.getContent().forEach(evaluated -> {
            relationMboService.findByEvaulated(tno, evaluated.getSno()).ifPresent(relation -> {
                relation.forEach(origin -> {
                    switch (origin.getRelation()) {
                    case "me":
                        relationMe.add(origin);
                        break;
                    case "1":
                        relation1.add(origin);
                        break;
                    case "2":
                        relation2.add(origin);
                        break;
                    case "3":
                        relation3.add(origin);
                        break;
                    }
                });
            });
        });

        model.addAttribute("relationMe", relationMe);
        model.addAttribute("relation1", relation1);
        model.addAttribute("relation2", relation2);
        model.addAttribute("relation3", relation3);
    }

    /**
     * 회차의 피평가자 등록 후보 명단을 REST로 읽어온다.
     * 
     * @param tno 회차 id
     * @return 피평가자 등록 후보 명단
     */
    @GetMapping("/evaluated/{tno}")
    @ResponseBody
    public ResponseEntity<Optional<List<Staff>>> getStaffForEvaluated(@PathVariable("tno") long tno) {
        log.info("get All Staff List Exclude Evaluated....");

        long cno = turnService.read(tno).map(Turn::getCno).orElse(null);
        return new ResponseEntity<>(staffService.getMboEvaluatedList(cno, tno), HttpStatus.OK);
    }

    /**
     * 회차 내의 해당 피평가자의 평가자 등록 후보 명단을 REST로 읽어온다.
     * 
     * @param tno 회차 id
     * @param sno 피평가자 id
     * @return 평가자 등록 후보 명단
     */
    @GetMapping("/evaluator/{tno}/{sno}")
    @ResponseBody
    public ResponseEntity<Optional<List<Staff>>> getStaffForEvaluator(@PathVariable("tno") long tno,
            @PathVariable("sno") long sno) {
        log.info("get All Staff List....");

        long cno = turnService.read(tno).map(Turn::getCno).orElse(null);
        return new ResponseEntity<>(staffService.getMboEvaluatorList(cno, tno, sno), HttpStatus.OK);
    }

    /**
     * 회차 내의 피평가자의 모든 관계 정보를 삭제한다.
     * 
     * @param tno  회차 id
     * @param sno  피평가자 id
     * @param vo   페이지 정보
     * @param rttr 재전송 정보
     * @return 관계 목록 페이지
     */
    @PostMapping("/removeRow")
    public String deleteEvaluatedInfo(long tno, long sno, PageVO vo, RedirectAttributes rttr) {
        log.info("deleteEvaluatedInfo by " + tno);

        relationMboService.findByEvaulated(tno, sno).ifPresent(list -> {
            list.forEach(relation -> {
                relationMboService.remove(relation.getRno());
            });
        });

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relationMbo/list";
    }

    /**
     * 관계 정보를 xl 업로드 한다.
     * 
     * @param tno        회차 id
     * @param deleteList 기존 관계 삭제 여부
     * @param uploadFile 업로드 파일
     * @param model      화면 전달 정보
     */
    @PostMapping("/xlUpload")
    @ResponseBody
    public void xlUpload(long tno, Boolean deleteList, MultipartFile uploadFile, Model model) {
        log.info("read file" + uploadFile);

        long cno = turnService.read(tno).map(Turn::getCno).orElse(null);

        if (deleteList == true) {
            relationMboService.findAllByTno(tno).ifPresent(list -> {
                list.forEach(relation -> {
                    relationMboService.remove(relation.getRno());
                });
            });
        }

        int iteration = 0;
        List<List<String>> allData = AboutExcel.readExcel(uploadFile);

        for (List<String> list : allData) {
            log.info("" + list);

            // 첫줄 건너띄기
            if (iteration == 0) {
                iteration++;
                continue;
            }

            // 피평가자 정보 공통설정
            String email = list.get(1);
            Optional<Staff> origin = staffService.findByEmail(email);

            if (!origin.isPresent()) {
                throw new IllegalArgumentException("피평가자 정보가 직원명단에 존재하지 않습니다." + email);
            }

            origin.ifPresent(evaluated -> {

                // 본인 평가 설정
                if (list.get(7).equals("Y")) {
                    RelationMbo relationMbo = new RelationMbo();

                    relationMbo.setEvaluated(evaluated);
                    relationMbo.setTno(tno);
                    // 개별 설정
                    relationMbo.setEvaluator(evaluated);
                    relationMbo.setRelation("me");
                    relationMboService.register(relationMbo);
                }

                // 1차 고과자 설정
                insertEvaluator(tno, cno, 8, "1", evaluated, list);
                // 2차 고과자 설정
                insertEvaluator(tno, cno, 9, "2", evaluated, list);
                // 3차 고과자 설정
                insertEvaluator(tno, cno, 10, "3", evaluated, list);
            });
        }
    }

    /**
     * 해당 열의 평가자 이름을 ,로 나눠서 등록한다.
     * 
     * @param tno       회차 id
     * @param cno       회사 id
     * @param lineNum   열 번호
     * @param relation  관계
     * @param evaluated 피평가자
     * @param list      평가자 이름 리스트
     */
    public void insertEvaluator(Long tno, Long cno, int lineNum, String relation, Staff evaluated, List<String> list) {
        // if 분기문에서 null check을 먼저 해야함. null인 값과 equals를 하는 것은 모순 이기 때문에 <-> check
        // equals && null (x)
        if (!(list.get(lineNum) == null) && !(list.get(lineNum).equals("N"))) {

            List<String> tmpList = new ArrayList<String>();

            String array[] = list.get(lineNum).split(",");
            tmpList = Arrays.asList(array);
            // step2 준비된 리스트를 루프 돌리기
            for (int i = 0; i < tmpList.size(); i++) {

                // 평가자 설정
                String name = tmpList.get(i).trim();
                Optional<Staff> origin = staffService.findByCnoAndName(cno, name);

                RelationMbo relationMbo = new RelationMbo();
                relationMbo.setEvaluated(evaluated);
                relationMbo.setTno(tno);
                relationMbo.setRelation(relation);
                // 평가자 정보 못 찾으면 null할당
                if (!origin.isPresent()) {
                    relationMbo.setEvaluator(null);
                }
                // 평가자 정보 찾았으면 할당
                origin.ifPresent(evaluator -> {
                    // 본인 이름이 평가자 정보에 들어있을 때는 null할당
                    if (evaluated.equals(evaluator)) {
                        evaluator = null;
                    }
                    relationMbo.setEvaluator(evaluator);
                });
                relationMboService.register(relationMbo);

            }
        }
    }

    /**
     * 회차 내의 관계 정보를 xl 다운로드 한다.
     * @param tno 회차 id
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
                fileName = URLEncoder.encode(company + "_relationMbo_" + format_time, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

            List<List<String>> xlList = new ArrayList<List<String>>();
            List<String> header = new ArrayList<String>();

            // header.add("idx");
            header.add("이름");
            header.add("이메일");
            header.add("직책");
            header.add("부문");
            header.add("부서");
            header.add("직군");
            header.add("계층");
            header.add("본인평가");
            header.add("1차고과자");
            header.add("2차고과자");
            header.add("3차고과자");

            xlList.add(header);

            // 일단 중복제거한 피평가자 명단 가져오고
            List<Staff> evaluatedList = relationMboService.findDintinctEavluatedByTno(tno);

            evaluatedList.forEach(evaluated -> {
                List<String> tmpList = new ArrayList<String>();

                // foreach에서 index를 얻기 위한..
                // int index = evaluatedList.indexOf(evaluated);
                // tmpList.add(Integer.toString(index + 1));

                tmpList.add(evaluated.getName());
                tmpList.add(evaluated.getEmail());
                tmpList.add(evaluated.getLevel());
                tmpList.add(evaluated.getDepartment1());
                tmpList.add(evaluated.getDepartment2());
                tmpList.add(evaluated.getDivision1());
                tmpList.add(evaluated.getDivision2());
                /* 관계 테이블 만들기 전체 명단에서 일칯하는 것만 리스트로 만든다 */
                List<String> relationMe = new ArrayList<String>();
                List<String> relation1 = new ArrayList<String>();
                List<String> relation2 = new ArrayList<String>();
                List<String> relation3 = new ArrayList<String>();
                relationMboService.findAllByTno(tno).get().forEach(relation -> {
                    String evaluator = Optional.ofNullable(relation.getEvaluator()).map(Staff::getName).orElse("null");
                    if (evaluated.getSno() == relation.getEvaluated().getSno()) {
                        switch (relation.getRelation()) {
                        case "me":
                            relationMe.add(evaluator);
                            break;
                        case "1":
                            relation1.add(evaluator);
                            break;
                        case "2":
                            relation2.add(evaluator);
                            break;
                        case "3":
                            relation3.add(evaluator);
                            break;
                        }
                    }
                });
                /* ./관계 테이블 만들기 */
                if (relationMe.isEmpty()) {
                    tmpList.add("N");
                } else {
                    tmpList.add("Y");
                }

                if (relation1.isEmpty()) {
                    tmpList.add("N");
                } else {
                    tmpList.add(relation1.toString().replace("[", "").replace("]", "").trim());
                }

                if (relation2.isEmpty()) {
                    tmpList.add("N");
                } else {
                    tmpList.add(relation2.toString().replace("[", "").replace("]", "").trim());
                }

                if (relation3.isEmpty()) {
                    tmpList.add("N");
                } else {
                    tmpList.add(relation3.toString().replace("[", "").replace("]", "").trim());
                }
                xlList.add(tmpList);
            });

            try {
                XSSFWorkbook workbook = AboutExcel.writeExcel(xlList);
                workbook.write(response.getOutputStream());
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}