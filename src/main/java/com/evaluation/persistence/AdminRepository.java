package com.evaluation.persistence;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Admin;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {

    Optional<List<Admin>> findAll(Sort sort);

}