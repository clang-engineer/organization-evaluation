package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Admin;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

/**
 * <code>AdminRepository</code> 객체는 Admin 객체의 영속화를 위해 표현한다.
 */
public interface AdminRepository extends CrudRepository<Admin, String> {

    /**
     * 전체 관리자 list를 불러온다.
     * @param sort 정렬 지정.
     * @return 관리자 리스트
     */
    Optional<List<Admin>> findAll(Sort sort);

}