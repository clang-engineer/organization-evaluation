package com.evaluation.security;

import com.evaluation.domain.Admin;
import com.evaluation.persistence.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomAdminService implements UserDetailsService {
    @Setter(onMethod_ = { @Autowired })
    private AdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin vo = adminRepo.findById(username).get();
        return vo == null ? null : new CustomAdmin(vo);

    }
}