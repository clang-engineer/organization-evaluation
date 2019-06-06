package com.evaluation.service.Impl;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Admin;
import com.evaluation.persistence.AdminRepository;
import com.evaluation.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Setter(onMethod_ = { @Autowired })
    AdminRepository adminRepo;

    public void register(Admin admin) {
        log.info("register : " + admin);

        adminRepo.save(admin);
    }

    public Optional<Admin> read(String uid) {
        log.info("read by : " + uid);

        return adminRepo.findById(uid);
    }

    public void modify(Admin admin) {
        log.info("modify : " + admin);

        adminRepo.findById(admin.getUid()).ifPresent(origin -> {
            origin.setUname(admin.getUname());
            origin.setUpw(admin.getUpw());
            origin.setEnabled(admin.getEnabled());
            origin.setRoles(admin.getRoles());
            origin.setUpdateId(admin.getUpdateId());

            adminRepo.save(origin);
        });
    }

    @Override
    public void remove(String uid) {
        log.info("remove by : " + uid);

        adminRepo.deleteById(uid);
    }

    @Override
    public Optional<List<Admin>> list() {
        Sort sort = new Sort(Sort.Direction.ASC, "writeDate");
        Optional<List<Admin>> result = adminRepo.findAll(sort);
        return result;

    }
}