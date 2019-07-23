package com.evaluation.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * <code>SecurityConfig</code> 객체는 스프링 시큐리티를 설정하는데 사용한다..
 */
@Slf4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    CustomAdminService customAdminService;

    /**
     * url 접근을 관리한다.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config...");

        http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/book/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/company/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/help/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/staff/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/department/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/level/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/division/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/infoSurvey/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/relationSurvey/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/question/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/comment/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/progress/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/infoMbo/**").hasAnyRole("MANAGER", "ADMIN");
        http.authorizeRequests().antMatchers("/relationMbo/**").hasAnyRole("MANAGER", "ADMIN");

        http.authorizeRequests().antMatchers("/survey/*").permitAll();
        http.authorizeRequests().antMatchers("/mbo/*").permitAll();

        http.formLogin().loginPage("/login").successHandler(new CustomLoginSuccessHandler());

        http.exceptionHandling().accessDeniedPage("/accessDenied");

        http.logout().logoutUrl("/logout").invalidateHttpSession(true);

        http.rememberMe().key("orez").userDetailsService(customAdminService).tokenRepository(getJDBCRepository())
                .tokenValiditySeconds(60 * 60 * 24);

    }

    private PersistentTokenRepository getJDBCRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    /**
     * 패스워드를 암호화한다.
     * 
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 전달 받은 정보를 security가 인식할 수 있도록 동작한다.
     * 
     * @param auth
     * @throws Exception
     */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("build auth global,,");
        auth.userDetailsService(customAdminService).passwordEncoder(passwordEncoder());
    }

}