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
 * <code>Help</code> 객체는 고객문의 정정보를 표현한다.
 */
@Entity
@Getter
@Setter
@Table(name = "tbl_help_desk")
public class HelpDesk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hno;

    private String surveyInfo;

    private String company;

    private String name;

    private String email;

    private String telephone;

    @Column(name = "content", length = 2000)
    private String content;

    private String complete;

    private String writeId;
    
    private String updateId;

    @CreationTimestamp
    private Timestamp writeDate;

    @UpdateTimestamp
    private Timestamp updateDate;

}