package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Admin;

public interface AdminService {
    public void register(Admin admin);

    public Optional<Admin> read(String uid);

    public void modify(Admin admin);

    public void remove(String uid);

    public List<Admin> list();
}
