package com.evaluation.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.evaluation.domain.Mbo;
import com.evaluation.domain.RelationMbo;
import com.evaluation.domain.Reply;
import com.evaluation.domain.Staff;
import com.evaluation.domain.embeddable.RatioValue;
import com.evaluation.service.BookService;
import com.evaluation.service.CompanyService;
import com.evaluation.service.DepartmentService;
import com.evaluation.service.MboService;
import com.evaluation.service.RelationMboService;
import com.evaluation.service.ReplyService;
import com.evaluation.service.StaffService;
import com.evaluation.service.TurnService;
import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <code>MboController</code>객체는 Mbo정보를 관리한다.
 */
@Controller
@RequestMapping("/mbo")
@Slf4j
@AllArgsConstructor
@Transactional
public class MboController {

    BookService bookService;

    CompanyService companyService;

    DepartmentService departmentService;

    MboService mboService;

    RelationMboService relationMboService;

    ReplyService replyService;

    TurnService turnService;

    StaffService staffService;

    /**
     * 메인 페이지를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param request 요청 정보 객체
     * @param rttr    재전송 정보
     * @param model   화면 전달 정보
     * @return mbo 메인 페이자
     */
    @GetMapping("/main")
    public String main(String company, Long tno, HttpServletRequest request, RedirectAttributes rttr, Model model) {
        log.info("mbo main by " + company);

        HttpSession session = request.getSession();

        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/mbo";
        }

        // 회사 정보
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("company", origin);
        });

        // 회차 정보
        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });

        Staff staff = (Staff) session.getAttribute("evaluator");
        long sno = staff.getSno();

        // 사용자의 부서 정보
        departmentService.findByTnoSno(tno, sno).ifPresent(list -> {
            model.addAttribute("department", list);
        });

        return "/mbo/main";
    }

    @GetMapping("/recentChange/{tno}/{page}")
    @ResponseBody
    public ResponseEntity<Optional<List<String>>> recentChange(@PathVariable("tno") long tno,
            @PathVariable("page") int page, HttpServletRequest request) {
        log.info("recentChange by " + tno + "/" + page);
        HttpSession session = request.getSession();
        Staff staff = (Staff) session.getAttribute("evaluator");
        long sno = staff.getSno();

        return new ResponseEntity<Optional<List<String>>>(mboService.recentChangeOfEvaluatedList(tno, sno, page),
                HttpStatus.OK);
    }

    /**
     * mbo목록 페이지를 읽어온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param request 요청 객체 정보
     * @param model   화면 전달 정보
     * @param rttr    재전송 정보
     * @return mbo 대상자 목록 페이지
     */
    @GetMapping("/list")
    public String list(String company, long tno, HttpServletRequest request, Model model, RedirectAttributes rttr) {
        log.info("mbo list by company" + company + "/" + tno);

        HttpSession session = request.getSession();
        // 밑에 또 사용할 일이 있어서 따로 객체 만듬.
        Staff evaluator = (Staff) session.getAttribute("evaluator");

        if (evaluator == null) {
            rttr.addAttribute("company", company);
            return "redirect:/mbo";
        }

        // 회사 정보
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("company", origin);
        });

        // 회차 정보
        turnService.read(tno).ifPresent(origin -> {
            model.addAttribute("turn", origin);
        });

        relationMboService.findByEvaluator(tno, evaluator.getSno()).ifPresent(relation -> {
            // 관계정보 전달
            model.addAttribute("evaluatedList", relation);

            // 관계 종류를 전달하기 위한 Set
            Set<String> relationList = new HashSet<>();
            relation.forEach(origin -> {
                relationList.add(origin.getRelation());
            });
            model.addAttribute("relationList", relationList);

            // 피평가자의 특정 정보를 얻기 위한 list와 for문
            List<List<String>> ratioList = new ArrayList<>();
            List<RelationMbo> relationMeList = new ArrayList<>();

            relation.forEach(origin -> {
                // 피평가자의 서베이 진행률을 얻기 위한
                mboService.ratioByTnoSno(tno, origin.getEvaluated().getSno()).ifPresent(ratio -> {
                    ratioList.addAll(ratio);
                });

                // 피평가자의 본인 평가 완료 여부를 얻기 위한
                relationMboService.findMeRelationByTnoSno(tno, origin.getEvaluated().getSno()).ifPresent(tmpRel -> {
                    relationMeList.add(tmpRel);
                });
            });
            model.addAttribute("ratioList", ratioList);
            model.addAttribute("relationMeList", relationMeList);
        });

        return "/mbo/list";
    }

    /**
     * 목표 페이지에서 새로 고침시 목록 페이지로 재전송한다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param request 요청 객체 정보
     * @param rttr    재전송 정보
     * @return mbo 대상자 목록 페이지
     */
    @GetMapping("/object")
    public String object(String company, long tno, HttpServletRequest request, RedirectAttributes rttr) {
        log.info("object redirect by " + company + "/" + tno);

        HttpSession session = request.getSession();
        if (session.getAttribute("evaluator") == null) {
            rttr.addAttribute("company", company);
            return "redirect:/mbo";
        }

        companyService.findByCompanyId(company).ifPresent(origin -> {
            rttr.addFlashAttribute("company", origin);
        });

        rttr.addAttribute("company", company);
        rttr.addAttribute("tno", tno);
        return "redirect:/mbo/list";
    }

    /**
     * 사용자의 목표 정보를 불러온다.
     * 
     * @param company 회사 이름
     * @param tno     회차 id
     * @param rno     관계 id
     * @param model   화면 전달 정보
     */
    // @GetMapping("/object")
    @PostMapping("/object")
    public void object(String company, long tno, Long rno, Model model) {
        log.info("object list by " + company + "/" + tno + "/" + rno);

        // 회사 정보
        companyService.findByCompanyId(company).ifPresent(origin -> {
            model.addAttribute("company", origin);
        });

        turnService.read(tno).ifPresent(turn -> {
            model.addAttribute("turn", turn);
            // 평가 단계에서 회답지 추가
            if (turn.getInfoMbo().getStatus().equals("see") || turn.getInfoMbo().getStatus().equals("count")) {
                // 회답지 추가
                Integer replyCode = turn.getInfoMbo().getReplyCode();
                if (replyCode != null) {
                    bookService.read(replyCode).ifPresent(book -> {
                        model.addAttribute("replyCodeList", book.getContents());
                    });
                }
                // 가중치 추가
                Integer weightCode = turn.getInfoMbo().getWeightCode();
                if (weightCode != null) {
                    bookService.read(weightCode).ifPresent(book -> {
                        model.addAttribute("weightCodeList", book.getContents());
                    });
                }
            }
        });

        relationMboService.read(rno).ifPresent(relation -> {
            // 관계에 대한 정보 추가
            model.addAttribute("relation", relation);

            // 본인평가 정보 전달
            relationMboService.findMeRelationByTnoSno(tno, relation.getEvaluated().getSno()).ifPresent(relationMe -> {
                model.addAttribute("relationMe", relationMe);
            });

            // 피평가자의 팀 목표 전달
            departmentService.findByDepartment(tno, relation.getEvaluated().getDepartment1(),
                    relation.getEvaluated().getDepartment2()).ifPresent(origin -> {
                        model.addAttribute("department", origin);
                    });

            // 피평가자의 목표 가져오기, 피평가자 sno와 tno로
            mboService.listByTnoSno(tno, relation.getEvaluated().getSno()).ifPresent(list -> {
                // finish Y인 목록과 N인 목록 구분 지음.
                List<ObjectWithReplyNum> objectList = new LinkedList<ObjectWithReplyNum>();
                List<ObjectWithReplyNum> removedList = new LinkedList<ObjectWithReplyNum>();

                for (int i = 0; i < list.size(); i++) {

                    // 댓글 수와 목표를 묶은 객체를 만들어서 list에 추가함.
                    ObjectWithReplyNum object = new ObjectWithReplyNum();
                    // 목표를 추가하고
                    object.mbo = list.get(i);
                    // 목표에 해당하는 댓글수를 가져와서 추가하고
                    replyService.listByMno(list.get(i).getMno()).ifPresent(origin -> {
                        object.replyNum = origin.size();
                    });

                    // 삭제,수정 된 목표와 아닌 것을 구분하고
                    if (list.get(i).getFinish().equals("Y")) {
                        objectList.add(object);
                    } else {
                        removedList.add(object);
                    }
                }

                model.addAttribute("objectList", objectList);
                model.addAttribute("removedList", removedList);

                // 댓글 리스트 목록으로 만들어 전달하기 현재 목표리스트의 mno와 일치하는 댓글만
                List<Reply> replyList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    replyService.listByMno(list.get(i).getMno()).ifPresent(origin -> {
                        replyList.addAll(origin);
                    });
                }
                model.addAttribute("replyList", replyList);
            });
        });
    }

    /**
     * <code>ObjectWithReplyNum</code>객체는 목표와 댓글수를 목표 목록에서 험께 표한하기 위해 사용한다.
     */
    public class ObjectWithReplyNum {
        public Mbo mbo;
        public int replyNum;
    }

    /**
     * 면담 내용을 REST로 작성한다.
     * 
     * @param rno  관계 id
     * @param step 단계 id
     * @param note 면담 내용
     * @return http 성공메시지
     */
    @PostMapping("/note/{rno}/{step}")
    public ResponseEntity<HttpStatus> noteCreate(@PathVariable("rno") long rno, @PathVariable("step") String step,
            String note) {
        log.info("regiter note by " + rno + "/" + step + "/" + note);

        relationMboService.read(rno).ifPresent(origin -> {
            origin.getComments().put(step, note);
            relationMboService.modify(origin);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 면담 내용을 REST로 읽어온다.
     * 
     * @param rno  관계 id
     * @param step 단계 id
     * @return 면담 내용
     */
    @GetMapping("/note/{rno}/{step}")
    @ResponseBody
    public ResponseEntity<String> noteRead(@PathVariable("rno") long rno, @PathVariable("step") String step) {
        log.info("read note by " + rno + step);

        Map<String, String> comments = relationMboService.read(rno).map(RelationMbo::getComments).orElse(null);
        String note = comments.get(step);

        return new ResponseEntity<String>(note, HttpStatus.OK);
    }

    /**
     * 화면에서 평가를 REST방식으로 제출한다. (select 변할 때마다 전송)
     * 
     * @param answer 회답
     * @param rttr   재전송 정보
     * @return http 성공 메시지
     */
    @PutMapping("/submit")
    @ResponseBody
    public ResponseEntity<HttpStatus> submit(@RequestBody Map<String, Object> answer, RedirectAttributes rttr) {
        log.info("evaluate by " + answer);
        long tmpRno = Long.parseLong(answer.get("rno").toString());
        String tmpFinish = String.valueOf(answer.get("finish"));
        // key-value를 전달하는 경우랑 finish만 전달하는 경우를 나눠서 처리함
        if (answer.containsKey("key")) {
            // parse variable csrf 때문에 string으로 안 받아짐. 때문에 object로 받아서 변환
            String tmpKey = String.valueOf(answer.get("key"));
            Gson gson = new Gson();
            RatioValue tmpValue = gson.fromJson(answer.get("value").toString(), RatioValue.class);

            relationMboService.read(tmpRno).ifPresent(origin -> {
                origin.setFinish(tmpFinish);
                origin.getAnswers().put(tmpKey, tmpValue);
                relationMboService.modify(origin);
            });
        } else {
            relationMboService.read(tmpRno).ifPresent(origin -> {
                origin.setFinish(tmpFinish);
                relationMboService.modify(origin);
            });
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}