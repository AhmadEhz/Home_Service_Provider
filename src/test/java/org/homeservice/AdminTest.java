package org.homeservice;

import org.homeservice.entity.Admin;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.junit.jupiter.api.*;
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

    static Service service1 = new Service("service1Name");
    static Service service2 = new Service("service2Name");
    static Service service3 = new Service("service3Name");
    static SubService subService1 = new SubService
            ("sub1forServ1", "sub1description", 200D, service1);
    static SubService subService2 = new SubService(
            "sub2forServ2", "sub2description", 250D, service2);
    static SubService subService3 = new SubService
            ("sub3forServ2", "sub3description", 225D, service2);
    static SubService subService4 = new SubService
            ("sub4forServ3", "sub4description", 150D, service3);

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

    @Test
    @Order(3)
    void addService() {
        services.serviceService.save(service1);
        services.serviceService.save(service2);
        services.serviceService.save(service3);
        assertEquals(service1, services.serviceService.findById(service1.getId()).get());
        assertEquals(service2, services.serviceService.findById(service2.getId()).get());
        assertEquals(service3, services.serviceService.findById(service3.getId()).get());
        assertEquals(service1, services.serviceService.findByName(service1.getName()).get());
    }

    @Test
    @Order(4)
    void addSubService() {
        services.subServiceService.save(subService1);
        services.subServiceService.save(subService2);
        services.subServiceService.save(subService3);
        services.subServiceService.save(subService4);
        assertEquals(subService1, services.subServiceService.findById(subService1.getId()).get());
        assertEquals(subService2, services.subServiceService.findById(subService2.getId()).get());
        assertEquals(subService3, services.subServiceService.findById(subService3.getId()).get());
        assertEquals(subService4, services.subServiceService.findById(subService4.getId()).get());
        service2 = services.serviceService.findById(service2.getId()).get();
        assertEquals(service2.getSubServices().size(),
                services.subServiceService.loadAllByService(service2.getId()).size());
    }
}
