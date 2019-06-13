package com.evaluation.controller;

import com.evaluation.service.Relation360Service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/progress/**")
@AllArgsConstructor
@Slf4j
public class ProgressController {

    Relation360Service relation360Service;

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
}