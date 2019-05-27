package com.evaluation.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.evaluation.domain.Admin;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class CustomAdmin extends User {

    private static final String ROLE_PREFIX = "ROLE_";

    private static final long serialVersionUID = 1L;

    private Admin admin;

    public CustomAdmin(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomAdmin(Admin vo) {
        super(vo.getUid(), vo.getUpw(), makeGrantedAuthority(vo.getRoles()));
        this.admin = vo;
    }

    private static Set<GrantedAuthority> makeGrantedAuthority(Set<String> roles) {
        Set<GrantedAuthority> list = new HashSet<>();
        
        roles.forEach(role -> log.info(ROLE_PREFIX + role));
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));

        return list;
    }
}