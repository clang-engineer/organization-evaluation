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
public class MBO implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mno;

    @Column(name = "object", length = 2000)
    private String object;

    @Column(name = "process", length = 2000)
    private String process;

    private Double ratio;

    // final이라는 이름으로 변수 하고 싶은데 스프링 상수 이므로 불가능! finish로
    private String finish = "Y";

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

    //mbo복사해서 쓰기 위해서, do단계에서 수정 시 기존 것 finish N으로 기록하기 위해!
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}