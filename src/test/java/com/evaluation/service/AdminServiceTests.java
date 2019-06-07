package com.evaluation.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * AdminServiceTests
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminServiceTests {

    @Setter(onMethod_ = { @Autowired })
    AdminService adminService;

    @Test
    public void testDeleteById() {
        adminService.remove("abc");
    }
}