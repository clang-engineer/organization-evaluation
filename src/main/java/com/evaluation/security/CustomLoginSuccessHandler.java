package com.evaluation.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * <code>CustomLoginSuccessHandler</code> 객체는 로그인 성공시의 동작을 통제한다.
 */
@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
        log.info("Login Success");

        List<String> roleNames = new ArrayList<>();

        auth.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        log.warn("ROLE NAMES" + roleNames);

        if (roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("company/list");
            return;
        }

        if (roleNames.contains("ROLE_MANAGER")) {
            response.sendRedirect("company/list");
            return;
        }

        if (roleNames.contains("ROLE_BASIC")) {
            response.sendRedirect("company/list");
            return;
        }

        response.sendRedirect("/");
    }
}