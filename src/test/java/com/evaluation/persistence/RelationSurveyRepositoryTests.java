package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.evaluation.domain.RelationSurvey;
import com.evaluation.domain.Staff;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class RelationSurveyRepositoryTests {

    @Autowired
    RelationSurveyRepository repo;

    @Autowired
    StaffRepository staffRepo;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Before
    public void testInsert() {
        Long[] arr = { 10L, 9L, 8L };

        Arrays.stream(arr).forEach(num -> {

            IntStream.range(1, 31).forEach(i -> {
                RelationSurvey relationSurvey = new RelationSurvey();

                Staff evaluated = new Staff();
                evaluated.setSno(1L);
                Staff evaluator = new Staff();
                evaluator.setSno(1L);
                evaluator.setEmail("test@test.com");
                relationSurvey.setEvaluated(evaluated);
                relationSurvey.setEvaluator(evaluator);
                if (i % 4 == 0) {
                    relationSurvey.setRelation("me");
                } else if (i % 4 == 1) {
                    relationSurvey.setRelation("1");
                } else if (i % 4 == 2) {
                    relationSurvey.setRelation("2");
                } else if (i % 4 == 3) {
                    relationSurvey.setRelation("3");
                }
                Map<String, Double> answers = new HashMap<String, Double>();
                answers.put("q1", 1.0);
                answers.put("q2", 2.0);
                answers.put("q3", 3.0);
                answers.put("q4", 4.0);
                answers.put("q5", 5.0);
                relationSurvey.setAnswers(answers);

                Map<String, String> comments = new HashMap<String, String>();
                comments.put("c1",
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industrageMaker including versions of Lorem Ipsum.");
                comments.put("c2",
                        "Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.");
                comments.put("c3", "12312312313(*&&(*^!@$*)!*@()*$&!@)&$&(*&!@&KJKJBSAVKJBJ");
                relationSurvey.setComments(comments);

                relationSurvey.setTno(1L);

                if (i % 2 == 0) {
                    relationSurvey.setFinish("Y");
                } else if (i % 2 == 1) {
                    relationSurvey.setFinish("N");
                }
                repo.save(relationSurvey);

            });
        });
    }

    @Test
    public void readTest() {
        Optional<RelationSurvey> result = repo.findById(3L);
        log.info("===>" + result.get().getEvaluated().getEmail());

    }

    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "rno");
        Page<RelationSurvey> result = repo.findAll(repo.makePredicate("evaluated", "102", 9L), pageable);
        log.info("PAGE : " + result.getPageable());

        log.info("----------------");
        // fetch 타입 체크...! lazy때문에 못 불러오는데, 나중에도 그런지 확인해보자.
        result.getContent().forEach(relationSurvey -> log.info("" + relationSurvey.getEvaluated().getEmail()));
    }

    @Test
    public void testFindEvaluatorByEvaulatedSno() {
        repo.findByEvaulated(1L, 1L).ifPresent(origin -> {
            origin.forEach(relation -> {
                log.info("" + relation.getRno());
            });
        });
    }

    @Test
    public void testGetDistinctEvaluatedListTest() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "rno");

        Page<Staff> result = repo.getDistinctEvaluatedList(1L, pageable);
        repo.getDistinctEvaluatedListByEvaluated(1L, null, pageable);
        repo.getDistinctEvaluatedListByEvaluator(1L, null, pageable);
        result.getContent().forEach(relationSurvey -> log.info("" + relationSurvey.getName()));
    }

    @Test
    public void testFindByEvaluator() {
        log.info("" + repo.findByEvaluatorEmail(1L, "test@test.com").isPresent());
    }

    @Test
    public void testFindByEvaulaor() {
        log.info("" + repo.findByEvaulator(1L, 1L));
    }

    @Test
    public void testFindAllByTno() {
        repo.findAllByTno(1L).ifPresent(list -> {
            list.forEach(relation -> {
                log.info("" + relation.getRno());
            });
        });
    }

    @Test
    public void testFindAllDistinctByTno() {
        repo.findDintinctEavluatedByTno(1L).forEach(staff -> {
            log.info("" + staff.getName());
        });
    }

    @Test
    public void testProgressOfSurevey() {
        repo.progressOfSurevey(1L).ifPresent(list -> {
            list.forEach(origin -> {
                log.info("" + origin);
            });
        });
    }
}