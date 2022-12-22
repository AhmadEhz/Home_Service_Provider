package org.homeservice;

import org.homeservice.entity.Customer;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {
    @Autowired
    Services services;

    static Customer customer1 = new Customer("customer1FName","customer1LName",
            "customer1UName","customer1Pass","customer1@Mail.com");
    static Customer customer2 = new Customer("customer2FName","customer2LName",
            "customer2UName","customer2Pass","customer2@Mail.com");
    static Customer customer3 = new Customer("customer1FName","customer1LName",
            "customer3UName","customer1Pass","customer3@Mail.com");

    @Test
    @Order(1)
    void save() {
        services.customerService.save(customer1);
        services.customerService.save(customer2);
        services.customerService.save(customer3);
        assertEquals(customer1,services.customerService.findById(customer1.getId()).get());
        assertEquals(customer2,services.customerService.findById(customer2.getId()).get());
        assertEquals(customer3,services.customerService.findById(customer3.getId()).get());
    }

    @Test
    @Order(2)
    void changePassword() {
        String newPassword = "customer1newPass";
        assertEquals(customer1.getPassword(),services.customerService.findById(customer1.getId()).get().getPassword());
        services.customerService.changePassword(customer1.getUsername(),customer1.getPassword(),newPassword);
        assertEquals(newPassword,services.customerService.findById(customer1.getId()).get().getPassword());
    }

    @Test
    @Order(3)
    void incorrectPassword() {
        String incorrectPassword = customer1.getPassword() + "in";
        assertThrows(CustomIllegalArgumentException.class, () -> services.customerService
                .changePassword(customer1.getUsername(),incorrectPassword,"newPassword"));
    }

}
