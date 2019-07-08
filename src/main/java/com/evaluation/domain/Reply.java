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
 * <code>Reply</code> 각 목표에 달린 댓글을 표현한다.
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_MBO_Reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rno;

    private String Comment;

    @Column(name = "mbo_mno")
    long mno;

    private String writeId;
    private String updateId;

    @CreationTimestamp
    private Timestamp writeDate;
    @UpdateTimestamp
    private Timestamp updateDate;
}