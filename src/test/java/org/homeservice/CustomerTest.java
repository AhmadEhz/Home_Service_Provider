package org.homeservice;

import org.homeservice.entity.Customer;
import org.homeservice.entity.SubService;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestComponent
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {
    Services services;

    @Autowired
    public CustomerTest(Services services) {
        this.services = services;
    }

    static Customer customer1 = new Customer("customer1FName","customer1LName",
            "customer1UName","customer1Pass","customer1@Mail.com");
    static Customer customer2 = new Customer("customer2FName","customer2LName",
            "customer2UName","customer2Pass","customer2@Mail.com");
    static Customer customer3 = new Customer("customer1FName","customer1LName",
            "customer3UName","customer1Pass","customer3@Mail.com");
    static org.homeservice.entity.Order order1 = new org.homeservice.entity.Order
            (200D,"order1Description", LocalDateTime.of(2022,12,25,12,30),
                    "Tehran",new SubService());

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
        customer1 = services.customerService.findById(customer1.getId()).get();
        assertEquals(newPassword,customer1.getPassword());
    }

    @Test
    @Order(3)
    void incorrectPassword() {
        String incorrectPassword = customer1.getPassword() + "in";
        assertThrows(CustomIllegalArgumentException.class, () -> services.customerService
                .changePassword(customer1.getUsername(),incorrectPassword,"newPassword"));
    }

    //Following test fails if run this class.
    @Test
    @Order(4)
    void setOrder() {

    }
}
