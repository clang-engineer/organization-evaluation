package com.evaluation.domain;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>Admin</code> 객체는 전체 시스템 관리자를 표현한다.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_admin")
public class Admin {

    @Id
    @Column(name = "uid")
    private String uid;

    private String upw;
    private String uname;
    private String enabled;

    @ElementCollection
    @CollectionTable(name = "tbl_admin_roles", joinColumns = @JoinColumn(name = "admin_uid"))
    @Column(name = "role")
    private Set<String> roles;

    private String writeId;
    private String updateId;

    @CreationTimestamp
    private Timestamp writeDate;
    @UpdateTimestamp
    private Timestamp updateDate;

}