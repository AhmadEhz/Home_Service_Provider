package org.homeservice;

import org.homeservice.entity.Admin;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestComponent
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class AdminTest {
    Services services;

    @Autowired
    public AdminTest(Services services) {
        this.services = services;
    }

    static Admin admin1 = new Admin("adminFirstName", "adminLastName"
            , "AdminUsername", "AdminPassword");

    @Test
    @Order(1)
    void save() {
        services.adminService.save(admin1);
        assertEquals(admin1, services.adminService.findById(admin1.getId()).orElse(null));
    }

    @Test
    @Order(2)
    void changePassword() {
        String newPassword = "adminNewPassword";
        services.adminService.changePassword(admin1.getUsername(), admin1.getPassword(), newPassword);
        admin1 = services.adminService.findById(admin1.getId()).get();
        assertEquals(admin1.getPassword(), newPassword);
    }

    @Test
    @Order(3)
    void checkPassword() {
        String password = services.adminService.findById(admin1.getId()).get().getPassword() + "ab";
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.adminService.changePassword(admin1.getUsername(), password, "newPass"));
    }
}
