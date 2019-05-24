package com.evaluation.persistence;

import java.util.List;

import com.evaluation.domain.Admin;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {

    List<Admin> findAll(Sort sort);

}