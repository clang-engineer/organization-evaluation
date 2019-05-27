package com.evaluation.domain;

import java.sql.Timestamp;

import javax.persistence.Embeddable;

@Embeddable
public class InfoSurvey {

	private String title;
	private String content;
	private String replyCode;
	private String status;

	private Timestamp startDate;
	private Timestamp endDate;

}
