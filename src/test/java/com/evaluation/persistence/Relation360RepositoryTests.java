package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.evaluation.domain.Relation360;
import com.evaluation.domain.Staff;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Relation360RepositoryTests {

    @Setter(onMethod_ = { @Autowired })
    Relation360Repository relation360Repo;

    @Setter(onMethod_ = { @Autowired })
    StaffRepository staffRepo;

    @Test
    public void diTest() {
        assertNotNull(relation360Repo);
    }

    @Test
    public void testInsert() {
        Long[] arr = { 10L, 9L, 8L };

        Arrays.stream(arr).forEach(num -> {

            IntStream.range(1, 31).forEach(i -> {
                Relation360 relation360 = new Relation360();

                Staff evaluated = staffRepo.findById((long) num + i).get();
                Staff evaluator = staffRepo.findById((long) num + i + 5).get();
                relation360.setEvaluated(evaluated);
                relation360.setEvaluator(evaluator);
                if (i % 4 == 0) {
                    relation360.setRelation("me");
                } else if (i % 4 == 1) {
                    relation360.setRelation("1");
                } else if (i % 4 == 2) {
                    relation360.setRelation("2");
                } else if (i % 4 == 3) {
                    relation360.setRelation("3");
                }
                Map<String, Integer> answers = new HashMap<String, Integer>();
                answers.put("q1", 1);
                answers.put("q2", 2);
                answers.put("q3", 3);
                answers.put("q4", 4);
                answers.put("q5", 5);
                relation360.setAnswers(answers);

                Map<String, String> comments = new HashMap<String, String>();
                comments.put("c1",
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industrageMaker including versions of Lorem Ipsum.");
                comments.put("c2",
                        "Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.Lorem Ipsum 은 단순히 인쇄 및 조판 업계에 대한 가짜 텍스트입니다. Lorem Ipsum은 알 수없는 프린터가 유형의 조리실을최근에는 Lorem Ipsum 버전을 비롯하여 Aldus PageMaker와 같은 데스크톱 게시 소프트웨어가 대중화되었습니다.");
                comments.put("c3", "12312312313(*&&(*^!@$*)!*@()*$&!@)&$&(*&!@&KJKJBSAVKJBJ");
                relation360.setComments(comments);

                relation360.setTno(num);

                if (i % 2 == 0) {
                    relation360.setFinish("Y");
                } else if (i % 2 == 1) {
                    relation360.setFinish("N");
                }
                relation360Repo.save(relation360);

            });
        });
    }

    @Test
    public void readTest() {
        Optional<Relation360> result = relation360Repo.findById(3L);
        log.info("===>" + result.get().getEvaluated().getEmail());

    }

    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "rno");
        Page<Relation360> result = relation360Repo.findAll(relation360Repo.makePredicate("evaluated", "102", 9L),
                pageable);
        log.info("PAGE : " + result.getPageable());

        log.info("----------------");
        // fetch 타입 체크...! lazy때문에 못 불러오는데, 나중에도 그런지 확인해보자.
        result.getContent().forEach(relation360 -> log.info("" + relation360.getEvaluated().getEmail()));
    }

    @Test
    public void getDistinctEvaluatedListTest() {
        Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "rno");

        Page<Staff> result = relation360Repo.getDistinctEvaluatedList(1L, pageable);
        result.getContent().forEach(relation360 -> log.info("" + relation360.getName()));
        // log.info("" + result.getContent());
    }

    @Test
    public void testFindEvaluatorByEvaulatedSno() {
        relation360Repo.findByEvaulatedSno(47972L, 1L).ifPresent(origin -> {
            origin.forEach(relation -> {
                log.info("" + relation.getRno());
            });
        });
    }

    @Test
    public void findInEvaluator() {
        log.info("" + relation360Repo.findInEvaluator(1L, "youm.huh@siliconmitus.com").isPresent());
    }

    @Test
    public void testFindByEvaulaordSno() {
        log.info("" + relation360Repo.findByEvaulaordSno(2L, 1L));
    }

    @Test
    public void findAllByTno() {
        relation360Repo.findAllbyTno(1L).ifPresent(list -> {
            list.forEach(relation -> {
                log.info("" + relation.getRno());
            });
        });
    }

    @Test
    public void findAllDistinctByTno() {
        // relation360Repo.findAllDintinctbyTno(1L).ifPresent(list -> {
        //     list.forEach(staff -> {
        //         log.info("" + staff.getName());
        //     });
        // });
        relation360Repo.findDintinctEavluatedbyTno(1L).forEach(staff -> {
            log.info("" + staff.getName());
        });
    }
}