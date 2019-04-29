package com.evaluation.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "tbl_company")
@EqualsAndHashCode(of = "cno")
@ToString(exclude = "turns")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cno;

	private String id;
	private String name;
	private String password;
	private String homepage;
	private String logo;
	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@OneToMany(mappedBy = "company", cascade=CascadeType.ALL)
	private List<Turn> turns;
}
