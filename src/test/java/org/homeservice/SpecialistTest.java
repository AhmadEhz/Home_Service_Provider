package org.homeservice;

import org.homeservice.entity.Specialist;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpecialistTest {
    @Autowired
    Services services;
    static Specialist specialist1 = new Specialist
            ("SP1FName", "SP1LName", "SP1UName", "SPE1Pass", "SP1@Mail.com");
    static Specialist specialist2 = new Specialist
            ("SP2FName", "SP2LName", "SP2UName", "SPE2Pass", "SP2@Mail.com");
    static Specialist specialist3 = new Specialist
            ("SP3FName", "SP3LName", "SP3UName", "SPE3Pass", "SP3@Mail.com");

    @Test
    @Order(1)
    void save() {
        services.specialistService.save(specialist1);
        services.specialistService.save(specialist2);
        services.specialistService.save(specialist3);
        assertEquals(specialist1, services.specialistService.findById(specialist1.getId()).get());
        assertEquals(specialist2, services.specialistService.findById(specialist2.getId()).get());
        assertEquals(specialist3, services.specialistService.findById(specialist3.getId()).get());
    }

    @Test
    @Order(2)
    void changePassword() {
        String newPassword = "SP1NewPass";
        services.specialistService.changePassword(specialist1.getUsername(), specialist1.getPassword(), newPassword);
        specialist1 = services.specialistService.findById(specialist1.getId()).get();
        assertEquals(specialist1.getPassword(), newPassword);
    }

}
