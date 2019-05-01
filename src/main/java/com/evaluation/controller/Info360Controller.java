package com.evaluation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/setDetail/*")
public class Info360Controller {
	
	@GetMapping("/360Info")
	public void detail() {
		
	}
	
}
