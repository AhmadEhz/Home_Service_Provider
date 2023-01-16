package org.homeservice;

import org.homeservice.entity.Admin;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.homeservice.util.exception.SpecialistNotAccessException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

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
    static SubService subService5 = new SubService("sub5forUnknownServ",
            "sub5description", 300D, new Service("unknownServiceName"));
    static SubService subService6 = new SubService("sub1forServ1", "not saved", 200D, service1);

    static Admin admin1 = new Admin("adminFirstName", "adminLastName"
            , "AdminUsername", "AdminPassword");

    @Test
    @Order(1)
    void save() {
        services.adminService.save(admin1);
        assertEquals(admin1, services.loadAdmin(admin1.getId()));
    }

    @Test
    @Order(2)
    void changePassword() {
        String newPassword = "adminNewPassword";
        services.adminService.changePassword(admin1, admin1.getPassword(), newPassword);
        admin1 = services.adminService.loadById(admin1.getId()).get();
        assertEquals(admin1.getPassword(), newPassword);
    }

    @Test
    @Order(3)
    void checkPassword() {
        String password = services.adminService.loadById(admin1.getId()).get().getPassword() + "ab";
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.adminService.changePassword(admin1, password, "newPass"));
    }

    @Test
    @Order(3)
    void addService() {
        services.serviceService.save(service1);
        services.serviceService.save(service2);
        services.serviceService.save(service3);
        assertEquals(service1, services.serviceService.loadById(service1.getId()).get());
        assertEquals(service2, services.serviceService.loadById(service2.getId()).get());
        assertEquals(service3, services.serviceService.loadById(service3.getId()).get());
        assertEquals(service1, services.serviceService.findByName(service1.getName()).get());
    }

    @Test
    @Order(4)
    void addSubService() {
        services.subServiceService.save(subService1);
        services.subServiceService.save(subService2);
        services.subServiceService.save(subService3);
        services.subServiceService.save(subService4);
        assertEquals(subService1, services.loadSubService(subService1.getId()));
        assertEquals(subService2, services.loadSubService(subService2.getId()));
        assertEquals(subService3, services.loadSubService(subService3.getId()));
        assertEquals(subService4, services.loadSubService(subService4.getId()));
        service2 = services.loadService(service2.getId());
        assertEquals(service2.getSubServices().size(),
                services.subServiceService.loadAllByService(service2.getId()).size());
    }

    @Test
    @Order(4)
    void checkSubServiceNotSaved() {
        assertThrows(NullPointerException.class, () -> services.subServiceService.save(subService5));
        assertThrows(NotFoundException.class, () ->
                services.subServiceService.save(subService5, subService5.getService().getName()));
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.subServiceService.save(subService6, subService6.getService().getName()));
    }

    //Following tests fails if run in this class. Run MainTest class.
    void addSpecialistToSubService() {
        services.subServiceSpecialistService.save(SpecialistTest.specialist1.getId(), subService1.getId());
        assertTrue(services.subServiceSpecialistService.isExist(SpecialistTest.specialist1.getId(), subService1.getId()));
        assertFalse(services.subServiceSpecialistService.isExist(SpecialistTest.specialist1.getId(), subService2.getId()));
        assertFalse(services.subServiceSpecialistService.isExist(SpecialistTest.specialist2.getId(), subService2.getId()));
        assertThrows(SpecialistNotAccessException.class, () ->
                services.subServiceSpecialistService.save(SpecialistTest.specialist2.getId(), subService2.getId()));
    }

    void removeSpecialistFromSubService() {
        assertFalse(services.subServiceSpecialistService.isExist(SpecialistTest.specialist3.getId(), subService1.getId()));
        services.subServiceSpecialistService.save(SpecialistTest.specialist3.getId(), subService1.getId());
        assertTrue(services.subServiceSpecialistService.isExist(SpecialistTest.specialist3.getId(), subService1.getId()));
        services.subServiceSpecialistService.delete(SpecialistTest.specialist3.getId(), subService1.getId());
        assertFalse(services.subServiceSpecialistService.isExist(SpecialistTest.specialist3.getId(), subService1.getId()));
    }
}
