package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Admin;

public interface AdminService {
    void register(Admin admin);

    Optional<Admin> read(String uid);

    void modify(Admin admin);

    void remove(String uid);

    Optional<List<Admin>> list();
}
