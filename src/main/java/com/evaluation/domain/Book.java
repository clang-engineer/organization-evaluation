package com.evaluation.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.evaluation.domain.embeddable.Content;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>Bokk</code> 객체는 survey나 mbo 회답 코드를 표현한다.
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bno;

	private String type;

	@Column(unique = true, nullable = false)
	private String title;

	@ElementCollection
	@CollectionTable(name = "tbl_book_contents", joinColumns = @JoinColumn(name = "book_bno"))
	@OrderColumn(name = "content_idx")
	private List<Content> contents;

	private String writeId;
	private String updateId;

	@CreationTimestamp
	private Timestamp writeDate;
	@UpdateTimestamp
	private Timestamp updateDate;
}
