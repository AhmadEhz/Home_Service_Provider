package org.homeservice;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
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

//    @Autowired
//    public MainTest(Services services) {
//        this.services = services;
//        this.adminTest = new AdminTest(services);
//        this.customerTest = new CustomerTest(services);
//        this.specialistTest = new SpecialistTest(services);
//    }

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
    @Order(3)
    void checkUserPassword() {
        adminTest.checkPassword();
        customerTest.changePassword();
    }

}
