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

    @PostMapping("/remove")
    public String remove(long rno, long tno, PageVO vo, RedirectAttributes rttr) {
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

    @GetMapping("/evaluated/{tno}")
    @ResponseBody
    public ResponseEntity<Optional<List<Staff>>> getStaffForEvaluated(@PathVariable("tno") long tno) {
        log.info("get All Staff List Exclude Evaluated....");

        long cno = turnService.read(tno).get().getCno();
        return new ResponseEntity<>(staffService.getMboEvaluatedList(cno, tno), HttpStatus.OK);
    }

    @GetMapping("/evaluator/{tno}/{sno}")
    @ResponseBody
    public ResponseEntity<Optional<List<Staff>>> getStaffForEvaluator(@PathVariable("tno") long tno,
            @PathVariable("sno") long sno) {
        log.info("get All Staff List....");

        long cno = turnService.read(tno).get().getCno();
        return new ResponseEntity<>(staffService.getMboEvaluatorList(cno, tno, sno), HttpStatus.OK);
    }

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

    @PostMapping("/xlUpload")
    @ResponseBody
    public void xlUpload(long tno, Boolean deleteList, MultipartFile uploadFile, Model model) {
        log.info("read file" + uploadFile);

        long cno = turnService.read(tno).get().getCno();

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
                insertEvaluator(list, tno, cno, 8, "1", evaluated);
                // 2차 고과자 설정
                insertEvaluator(list, tno, cno, 9, "2", evaluated);
                // 3차 고과자 설정
                insertEvaluator(list, tno, cno, 10, "3", evaluated);
            });
        }
    }

    // 각 열의 ,로 연결된 고과자를 쪼개서 삽입하는 함수! 피평가자는 위에서 스태프로 체크 되기 때문에 삽입해줌. 각 고과자리스트, tno,
    // cno, 고과자가 위치한 열, 관계를 함께 삽입해줌.
    public void insertEvaluator(List<String> list, Long tno, Long cno, int lineNum, String relation, Staff evaluated) {
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