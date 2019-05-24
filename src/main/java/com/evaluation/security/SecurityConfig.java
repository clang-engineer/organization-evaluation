package com.evaluation.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    CustomAdminService customAdminService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config...");

        http.authorizeRequests().antMatchers("/company/**").permitAll();
        http.authorizeRequests().antMatchers("/book/**").hasRole("MANAGER");

        http.formLogin().loginPage("/admin/login");

        http.exceptionHandling().accessDeniedPage("/admin/accessDenied");

        http.logout().logoutUrl("/admin/logout").invalidateHttpSession(true);

        http.rememberMe().key("orez").userDetailsService(customAdminService).tokenRepository(getJDBCRepository())
                .tokenValiditySeconds(60 * 60 * 24);

    }

    private PersistentTokenRepository getJDBCRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("build auth global,,");
        auth.userDetailsService(customAdminService).passwordEncoder(passwordEncoder());
    }

}