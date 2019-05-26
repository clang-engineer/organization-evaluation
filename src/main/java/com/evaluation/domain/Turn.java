package com.evaluation.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
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

	@ElementCollection
	@CollectionTable(name = "tbl_turn_types", joinColumns = @JoinColumn(name = "turn_tno"))
	@OrderColumn(name = "type_idx")
	@Column(name = "type_name")
	private List<String> types;

	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_cno")
	private Company company;

}
