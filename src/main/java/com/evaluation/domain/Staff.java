package com.evaluation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_staff")
public class Staff {

	@Id
	private String email;
	private String id;
	private String password;
	private String name;
	private String department1;
	private String department2;
	private String level;
	private String division1;
	private String division2;
	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "company_cno")
	private Company company;

}
