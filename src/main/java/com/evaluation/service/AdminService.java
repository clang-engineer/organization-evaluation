package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Admin;

/**
 * <code>AdminService</code> 객체는 Admin객체 sevice 계층의 interface를 표현한다.
 */
public interface AdminService {
    /**
     * Admin 객체를 등록한다.
     * 
     * @param admin admin 객체
     */
    void register(Admin admin);

    /**
     * Admin 객체를 읽어온다.
     * 
     * @param uid admin id
     * @return admin 객체
     */
    Optional<Admin> read(String uid);

    /**
     * Admin 객체를 수정한다.
     * 
     * @param admin admin 객체
     */
    void modify(Admin admin);

    /**
     * Admin 객체를 삭제한다.
     * 
     * @param uid admin id
     */
    void remove(String uid);

    /**
     * Admin 객체 리스트를 전달한다.
     * 
     * @return admin 객체 리스트
     */
    Optional<List<Admin>> list();
}
