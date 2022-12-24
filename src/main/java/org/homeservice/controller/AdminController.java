package org.homeservice.controller;

import org.homeservice.service.AdminService;
import org.homeservice.service.ServiceService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    private AdminService adminService;
    private SpecialistService specialistService;
    private ServiceService serviceService;
    private SubServiceService subServiceService;
}
