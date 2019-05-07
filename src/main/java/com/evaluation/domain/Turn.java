package com.evaluation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "tbl_turn")
@EqualsAndHashCode(of = "tno")
@ToString(exclude = "company")
public class Turn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tno;

	private String title;

	private String type;

	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;

}
