package com.evaluation.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.evaluation.domain.Admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;

@RunWith(SpringRunner.class)
@SpringBootTest
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
            List<String> role = new ArrayList<String>();
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
}