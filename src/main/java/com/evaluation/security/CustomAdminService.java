package com.evaluation.security;

import com.evaluation.domain.Admin;
import com.evaluation.persistence.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomAdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin vo = adminRepo.findById(username).get();
        return vo == null ? null : new CustomAdmin(vo);

    }
}