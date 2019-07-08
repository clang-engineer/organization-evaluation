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
 * <code>Level</code> 객체는 회사의 직급 정보를 표현한다.
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_level")
public class Level {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lno;

	private String content;

	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@Column(name = "company_cno")
	private long cno;

}
