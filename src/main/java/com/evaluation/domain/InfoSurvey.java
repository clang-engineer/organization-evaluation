package com.evaluation.domain;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class InfoSurvey {

	private String title;
	private String content;
	
	private String replyCode;
	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime endDate;

}
