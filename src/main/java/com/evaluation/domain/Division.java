package com.evaluation.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>Division</code>객체는 설문 문항을 위한 회사의 구분 정보를 표현한다.
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_division")
public class Division {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dno;

	private String division1;
	private String division2;

	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@Column(name = "company_cno")
	private long cno;

}
