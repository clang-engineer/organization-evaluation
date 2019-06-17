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

@Getter
@Setter
@Entity
@Table(name = "tbl_MBO")
public class MBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mno;

    private String title;
    private String content;

    private String ratio;
    private String finish;

    private String writeId;
    private String updateId;

    @CreationTimestamp
    private Timestamp writeDate;
    @UpdateTimestamp
    private Timestamp updateDate;

    @Column(name = "turn_tno")
    private long tno;
    @Column(name = "staff_sno")
    private long sno;
}