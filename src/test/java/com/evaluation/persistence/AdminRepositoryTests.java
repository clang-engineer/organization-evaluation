package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import com.evaluation.domain.Admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class AdminRepositoryTests {

    @Setter(onMethod_ = { @Autowired })
    AdminRepository adminRepo;

    @Setter(onMethod_ = { @Autowired })
    PasswordEncoder pwEncoder;

    @Test
    public void testDI() {
        assertNotNull(adminRepo);
    }

    @Test
    public void testInsert() {
        for (int i = 0; i <= 100; i++) {
            Admin admin = new Admin();
            admin.setUid("uid" + i);
            admin.setUpw(pwEncoder.encode("uid" + i));
            admin.setUname("uname" + i);
            admin.setEnabled("enabled");
            Set<String> role = new HashSet<String>();
            if (i <= 30) {
                role.add("BASIC");
            } else if (i <= 60) {
                role.add("MANAGER");
            } else {
                role.add("ADMIN");
            }
            admin.setRoles(role);

            adminRepo.save(admin);
        }
    }

    @Test
    public void testGetRoles() {

        Admin admin = adminRepo.findById("test").get();
        log.info("===============================" + admin);
        Set<String> roles = admin.getRoles();
        log.info("===>" + roles.getClass());
        roles.forEach(role -> log.info("" + role));
    }
}