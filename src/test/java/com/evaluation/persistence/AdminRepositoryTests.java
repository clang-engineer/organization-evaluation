package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import com.evaluation.domain.Admin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class AdminRepositoryTests {

    @Autowired
    AdminRepository repo;

    @Autowired
    PasswordEncoder pwEncoder;

    @Test
    public void testDI() {
        assertNotNull(repo);
    }

    @Before
    public void testInsert() {
        // create
        for (int i = 0; i <= 11; i++) {
            Admin admin = new Admin();
            admin.setUid("uid" + i);
            admin.setUpw(pwEncoder.encode("uid" + i));
            admin.setUname("uname" + i);
            admin.setEnabled("enabled");
            Set<String> role = new HashSet<String>();
            if (i == 0) {
                role.add("BASIC");
            } else if (i == 1) {
                role.add("MANAGER");
            } else {
                role.add("ADMIN");
            }
            admin.setRoles(role);

            repo.save(admin);
        }
    }

    @Test
    public void testRead() {
        // read
        repo.findAll().forEach(origin -> {
            log.info("" + origin);
        });

        Admin admin = repo.findById("uid0").get();
        Set<String> roles = admin.getRoles();
        log.info("===>" + roles.getClass());
        roles.forEach(role -> log.info("" + role));
    }

    @Test
    public void testUpdate() {
        // update
        repo.findById("uid0").ifPresent(origin -> {
            origin.setEnabled("N");
            repo.save(origin);
        });
    }

    @Test
    public void testDelete() {
        // delete
        repo.deleteById("uid0");
    }
}