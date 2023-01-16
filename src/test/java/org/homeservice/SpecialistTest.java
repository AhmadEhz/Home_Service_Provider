package org.homeservice;

import org.homeservice.entity.Bid;
import org.homeservice.entity.OrderStatus;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.SpecialistNotAccessException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
@TestComponent
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpecialistTest {

    Services services;

    @Autowired
    public SpecialistTest(Services services) {
        this.services = services;
    }

    static Specialist specialist1 = new Specialist
            ("SP1FName", "SP1LName", "SP1UName", "SPE1Pass", "SP1@Mail.com");
    static Specialist specialist2 = new Specialist
            ("SP2FName", "SP2LName", "SP2UName", "SPE2Pass", "SP2@Mail.com");
    static Specialist specialist3 = new Specialist
            ("SP3FName", "SP3LName", "SP3UName", "SPE3Pass", "SP3@Mail.com");
    static Bid bid1 = new Bid(250D,
            LocalDateTime.of(2023, 1, 13, 11, 0),
            LocalDateTime.of(2023, 1, 13, 14, 30));
    static Bid bid2 = new Bid(10D,
            LocalDateTime.of(2023, 1, 15, 10, 30),
            LocalDateTime.of(2023, 1, 16, 12, 30));

    @Test
    @Order(1)
    void save() {
        services.specialistService.save(specialist1);
        services.specialistService.save(specialist2);
        services.specialistService.save(specialist3);
        assertEquals(specialist1, services.loadSpecialist(specialist1.getId()));
        assertEquals(specialist2, services.loadSpecialist(specialist2.getId()));
        assertEquals(specialist3, services.loadSpecialist(specialist3.getId()));
    }

    @Test
    @Order(2)
    void changePassword() {
        String newPassword = "SP1NewPass";
        services.specialistService.changePassword(specialist1, specialist1.getPassword(), newPassword);
        specialist1 = services.specialistService.findById(specialist1.getId()).get();
        assertEquals(specialist1.getPassword(), newPassword);
    }

    @Test
    @Order(2)
    void setAvatar() {
        File jpgFile = new File("/home/ahmad/Pictures/example.jpg");
        File pngFile = new File("/home/ahmad/Pictures/example.png");
        File jpg300KB = new File("/home/ahmad/Pictures/example300kb.jpg");
        byte[] avatarBytes = null;
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.specialistService.addAvatar(specialist1.getId(), pngFile));
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.specialistService.addAvatar(specialist1.getId(), jpg300KB));
        services.specialistService.addAvatar(specialist1.getId(), jpgFile);
        try (FileInputStream fileInputStream = new FileInputStream(jpgFile)) {
            avatarBytes = fileInputStream.readAllBytes();
        } catch (IOException e) {
            fail("Fail to load image");
        }
        assertArrayEquals(avatarBytes, services.loadSpecialist(specialist1.getId()).getAvatar());
    }

    @Test
    @Order(3)
    void verifySpecialist() {
        services.specialistService.verifySpecialist(specialist1.getId());
        services.specialistService.verifySpecialist(specialist3.getId());
        assertEquals(SpecialistStatus.ACCEPTED, services.loadSpecialist
                (specialist1.getId()).getStatus());
        assertTrue(services.loadSpecialist
                (specialist3.getId()).isVerified());
    }

    void setBid() {
        assertFalse(services.loadSpecialist(specialist2.getId()).isVerified());
        assertThrows(SpecialistNotAccessException.class,
                () -> services.bidService.save(bid1, CustomerTest.order1.getId(), specialist2));

        assertEquals(OrderStatus.WAITING_FOR_BID, services.loadOrder(CustomerTest.order1.getId()).getStatus());
        services.bidService.save(bid1, CustomerTest.order1.getId(), specialist1);
        assertEquals(OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST, services.loadOrder(CustomerTest.order1.getId()).getStatus());
        //Order status changed to "Waiting to choose Specialist" after save Bid. Order need to refresh.
        CustomerTest.order1 = services.loadOrder(CustomerTest.order1.getId());
        bid1.setOrder(services.loadOrder(bid1.getOrder().getId()));

        assertEquals(bid1, services.loadBid(bid1.getId()));
    }

    void setBidWithPriceLowerThanBasePrice() {
        assertThrows(CustomIllegalArgumentException.class,
                () -> services.bidService.save(bid2, CustomerTest.order1.getId(), specialist3));
    }


}
