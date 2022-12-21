package org.homeservice;

import org.homeservice.entity.Admin;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class AdminTest {
    @Autowired
    Services services;
    
    @Test
    @Order(1)
    void test() {

    }
}
