package com.evaluation.domain.embeddable;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>InfoSurvey</code> 객체는 Turn 객체에 속하는 Survey 정보를 표현한다
 */
@Embeddable
@Getter
@Setter
public class InfoSurvey {

	private String title;
	private String content;

	private Integer replyCode;
	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime endDate;

}
