package com.evaluation.persistence;

import com.evaluation.domain.Admin;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {

}