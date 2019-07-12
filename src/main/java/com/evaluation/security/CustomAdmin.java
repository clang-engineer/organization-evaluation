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

/**
 * <code>CustomAdmin</code> 객체는 User를 상속받고, 생성자로 전달 받은 정보를 User객체화 한다
 */
@Getter
@Setter
public class CustomAdmin extends User {

    private static final String ROLE_PREFIX = "ROLE_";

    private static final long serialVersionUID = 1L;

    private Admin admin;

    /**
     * 부모 생성자(User)를 호출한다.
     * 
     * @param username    사용자 id
     * @param password    passsword
     * @param authorities 권한을 담은 객체
     */
    public CustomAdmin(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    /**
     * 권한 객체를 받아서 security 객체로 만든다.
     * 
     * @param vo 권한 정보
     */
    public CustomAdmin(Admin vo) {
        super(vo.getUid(), vo.getUpw(), makeGrantedAuthority(vo.getRoles()));
        this.admin = vo;
    }

    /**
     * spring security가 인지할 수 있는 형태(SimpleGrantedAuthority)로 권한객체를 만든다.
     * 
     * @param roles
     * @return security용 권한 객체
     */
    private static Set<GrantedAuthority> makeGrantedAuthority(Set<String> roles) {
        Set<GrantedAuthority> list = new HashSet<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));

        return list;
    }
}