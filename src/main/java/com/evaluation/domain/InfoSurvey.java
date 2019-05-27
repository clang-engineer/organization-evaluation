package com.evaluation.domain;

import java.sql.Timestamp;

import javax.persistence.Embeddable;

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

	private Timestamp startDate;
	private Timestamp endDate;

}
