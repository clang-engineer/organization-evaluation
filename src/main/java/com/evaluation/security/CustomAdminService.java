package com.evaluation.security;

import com.evaluation.domain.Admin;
import com.evaluation.persistence.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <code>CustomAdminService</code> 객체는 UserDetailService를 이행하고,
 * loadUserByUsername 메소드를 구현한다.
 * 
 */
@Service
@Transactional
public class CustomAdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepo;

    /**
     * 존재하는 아이디면 security용 객체(CustomAdmin)를 리턴한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin vo = adminRepo.findById(username).get();
        return vo == null ? null : new CustomAdmin(vo);

    }
}