package com.evaluation.domain;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import com.evaluation.domain.embeddable.InfoMbo;
import com.evaluation.domain.embeddable.InfoSurvey;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_turn")
@SecondaryTables({
		@SecondaryTable(name = "tbl_turn_infoSurvey", pkJoinColumns = @PrimaryKeyJoinColumn(name = "turn_tno", referencedColumnName = "tno")),
		@SecondaryTable(name = "tbl_turn_infoMbo", pkJoinColumns = @PrimaryKeyJoinColumn(name = "turn_tno", referencedColumnName = "tno")), })
@EqualsAndHashCode(of = "tno")
public class Turn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tno;

	private String title;

	@ElementCollection
	@CollectionTable(name = "tbl_turn_types", joinColumns = @JoinColumn(name = "turn_tno"))
	@Column(name = "type")
	private Set<String> types;

	@ElementCollection
	@CollectionTable(name = "tbl_question_comments", joinColumns = @JoinColumn(name = "turn_tno"))
	@OrderColumn(name = "comment_idx")
	@Column(name = "comment")
	private List<String> comments;

	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;

	@Column(name = "company_cno")
	private Long cno;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "title", column = @Column(table = "tbl_turn_infoSurvey")),
			@AttributeOverride(name = "content", column = @Column(table = "tbl_turn_infoSurvey", length = 2000)),
			@AttributeOverride(name = "replyCode", column = @Column(table = "tbl_turn_infoSurvey")),
			@AttributeOverride(name = "status", column = @Column(table = "tbl_turn_infoSurvey")),
			@AttributeOverride(name = "startDate", column = @Column(table = "tbl_turn_infoSurvey")),
			@AttributeOverride(name = "endDate", column = @Column(table = "tbl_turn_infoSurvey")) })
	private InfoSurvey infoSurvey;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "title", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "content", column = @Column(table = "tbl_turn_infoMbo", length = 2000)),
			@AttributeOverride(name = "replyCode", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "weightCode", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "status", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "startDate", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "endDate", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "step1Start", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "step1End", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "step2Start", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "step2End", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "step3Start", column = @Column(table = "tbl_turn_infoMbo")),
			@AttributeOverride(name = "step3End", column = @Column(table = "tbl_turn_infoMbo")) })
	private InfoMbo infoMbo;
}