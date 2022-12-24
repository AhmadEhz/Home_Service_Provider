package org.homeservice.controller;

import org.homeservice.entity.Admin;
import org.homeservice.service.AdminService;
import org.homeservice.service.ServiceService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final SpecialistService specialistService;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    public AdminController(AdminService adminService, SpecialistService specialistService,
                           ServiceService serviceService, SubServiceService subServiceService) {
        this.adminService = adminService;
        this.specialistService = specialistService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
    }

    @PostMapping("/save")
    void save(@RequestBody Admin admin) {
        adminService.save(admin);
    }
}
