package org.homeservice;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Component
@Import({AdminTest.class,CustomerTest.class,SpecialistTest.class})
public class MainTest {
    Services services;
    AdminTest adminTest;
    CustomerTest customerTest;
    SpecialistTest specialistTest;

    @Autowired
    public MainTest(Services services, AdminTest adminTest, CustomerTest customerTest, SpecialistTest specialistTest) {
        this.services = services;
        this.adminTest = adminTest;
        this.customerTest = customerTest;
        this.specialistTest = specialistTest;
    }

    @Test
    @Order(1)
    void saveUsers() {
        adminTest.save();
        customerTest.save();
        specialistTest.save();
    }
    @Test
    @Order(2)
    void changeUserPassword() {
        adminTest.changePassword();
        customerTest.changePassword();
        specialistTest.changePassword();
    }
    @Test
    @Order(2)
    void setAvatar() {
        specialistTest.setAvatar();
    }

    @Test
    @Order(3)
    void checkUserPassword() {
        adminTest.checkPassword();
        customerTest.checkPassword();
    }

    @Test
    @Order(3)
    void addServicesAndSubServices() {
        adminTest.addService();
        adminTest.addSubService();
        adminTest.checkSubServiceNotSaved();
    }

    @Test
    @Order(4)
    void addOrder() {
        customerTest.addOrder();
        customerTest.addOrderWithLowerOfferPrice();
        customerTest.addOrderWithNotFoundCustomerAndSubService();
    }
    @Test
    void showAllSubServiceForAService() {
        customerTest.showSubServiceForAService();
    }
    @Test
    @Order(4)
    void verifySpecialist() {
        specialistTest.verifySpecialist();
    }

    @Test
    @Order(5)
    void addAndRemoveSpecialistFromSubService() {
        adminTest.addSpecialistToSubService();
        adminTest.removeSpecialistFromSubService();
    }

    @Test
    @Order(6)
    void setBidBySpecialist() {
        specialistTest.setBid();
        specialistTest.setBidWithPriceLowerThanBasePrice();
    }
    @Test
    @Order(7)
    void selectABidForOrder() {
        customerTest.selectBidForOrder();
    }
    @Test
    @Order(8)
    void showAllBidsOrderByPriceAndSpecialistScore() {
        customerTest.showAllBidsOrderByPriceAndSpecialistScore();
    }
    @Test
    @Order(9)
    void setOrderToStarted() {
        customerTest.startOrder();
    }
}
