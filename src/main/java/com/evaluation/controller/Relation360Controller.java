package com.evaluation.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;
import com.evaluation.function.AboutExcel;
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
@RequestMapping("/relation360/*")
@Slf4j
@AllArgsConstructor
@Transactional
public class Relation360Controller {

    Relation360Service relation360Service;

    StaffService staffService;

    TurnService turnService;

    @PostMapping("/register")
    public String register(Relation360 relation360, PageVO vo, RedirectAttributes rttr) {
        log.info("register " + relation360.getEvaluated().getSno());

        relation360Service.register(relation360);

        rttr.addFlashAttribute("msg", "register");
        rttr.addAttribute("tno", relation360.getTno());
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relation360/list";
    }

    @PostMapping("/remove")
    public String remove(long rno, long tno, PageVO vo, RedirectAttributes rttr) {
        log.info("remove " + tno + rno);

        relation360Service.remove(rno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relation360/list";
    }

    @GetMapping("/list")
    public void getList(long tno, PageVO vo, Model model) {
        log.info("getList by " + tno);

        model.addAttribute("tno", tno);

        // 우선 중복 제거한 피평가자 paging처리해서 구함
        Page<Staff> result = relation360Service.getDistinctEvaluatedList(tno, vo);
        model.addAttribute("result", new PageMaker<>(result));

        // 출력된 피평가자들의 sno로 relation table을 만듬.
        List<Relation360> relationTable = new ArrayList<>();
        result.getContent().forEach(origin -> {
            List<Relation360> tmpList = relation360Service.findRelationByEvaulatedSno(origin.getSno(), tno);
            relationTable.addAll(tmpList);
        });
        model.addAttribute("relationTable", relationTable);

    }

    @PostMapping("/removeRow")
    public String deleteEvaluatedInfo(long tno, long sno, PageVO vo, RedirectAttributes rttr) {
        log.info("deleteEvaluatedInfo by " + tno);

        relation360Service.deleteEvaluatedInfo(tno, sno);

        rttr.addFlashAttribute("msg", "remove");
        rttr.addAttribute("tno", tno);
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/relation360/list";
    }

    @PostMapping("/upload")
    @ResponseBody
    public void xlUpload(long tno, Boolean deleteList, MultipartFile uploadFile, Model model) {
        log.info("read file" + uploadFile);

        long cno = turnService.get(tno).get().getCno();

        if (deleteList == true) {
            relation360Service.deleteAllRelationByTno(tno);
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
            String email = list.get(2);
            Optional<Staff> origin = staffService.readByCnoAndEmail(cno, email);

            if (!origin.isPresent()) {
                throw new IllegalArgumentException("피평가자 정보가 직원명단에 존재하지 않습니다." + email);
            }

            origin.ifPresent(evaluated -> {

                // 본인 평가 설정
                if (list.get(8).equals("Y")) {
                    Relation360 relation360 = new Relation360();

                    relation360.setEvaluated(evaluated);
                    relation360.setTno(tno);
                    // 개별 설정
                    relation360.setEvaluator(evaluated);
                    relation360.setRelation("me");
                    relation360Service.register(relation360);
                }

                // 1차 고과자 설정
                insertEvaluator(list, tno, cno, 9, "1", evaluated);
                // 2차 고과자 설정
                insertEvaluator(list, tno, cno, 10, "2", evaluated);
                // 3차 고과자 설정
                insertEvaluator(list, tno, cno, 11, "3", evaluated);
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
                String name = tmpList.get(i);
                Optional<Staff> origin = staffService.readByCnoAndName(cno, name);

                Relation360 relation360 = new Relation360();
                relation360.setEvaluated(evaluated);
                relation360.setTno(tno);
                relation360.setRelation(relation);
                //평가자 정보 못 찾으면 null할당
                if (!origin.isPresent()) {
                    relation360.setEvaluator(null);
                }
                // 평가자 정보 찾았으면 할당
                origin.ifPresent(evaluator -> {
                    //본인 이름이 평가자 정보에 들어있을 때는 null할당
                    if (evaluated.equals(evaluator)) {
                        evaluator = null;
                    }
                    relation360.setEvaluator(evaluator);
                });
                relation360Service.register(relation360);

            }
        }
    }
}