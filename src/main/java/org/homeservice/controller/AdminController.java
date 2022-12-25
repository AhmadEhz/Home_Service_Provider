package org.homeservice.controller;

import org.homeservice.entity.Admin;
import org.homeservice.dto.SubServiceDto;
import org.homeservice.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final SpecialistService specialistService;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;
    private final SubServiceSpecialistService subServiceSpecialistService;

    public AdminController(AdminService adminService, SpecialistService specialistService,
                           ServiceService serviceService, SubServiceService subServiceService, SubServiceSpecialistService subServiceSpecialistService) {
        this.adminService = adminService;
        this.specialistService = specialistService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
        this.subServiceSpecialistService = subServiceSpecialistService;
    }

    @PostMapping("/save")
    void save(@RequestBody Admin admin) {
        adminService.save(admin);
    }

    @PutMapping("/change-password")
    void changePassword(@RequestBody Map<String, String> map) {
        adminService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @PostMapping("/service/save")
    void saveService(@RequestBody String serviceName) {
        serviceService.save(serviceName);
    }

    @PostMapping("/subservice/save")
    void saveSubService(@RequestBody SubServiceDto subServiceDTO) {
        subServiceService.save(subServiceDTO.getSubService(), subServiceDTO.getServiceName());
    }

    @PutMapping("/specialist/verify")
    void verifySpecialist(@RequestParam Long specialistId) {
        specialistService.verifySpecialist(specialistId);
    }

    @PutMapping("subservice/edit-description/{id}")
    void editSubServiceDescription(@RequestBody String description, @PathVariable Long id) {
        subServiceService.editDescription(description, id);
    }

    @PutMapping("subservice/edit-basePrice/{id}")
    void editSubServiceBasePrice(@RequestBody Double basePrice, @PathVariable Long id) {
        subServiceService.editBasePrice(basePrice, id);
    }

    @PutMapping("specialist/add-to-Subservice")
    void addSpecialistToSubService(@RequestBody Map<String, Long> map) {
        subServiceSpecialistService.save(map.get("specialistId"), map.get("subServiceId"));
    }

    @PutMapping("specialist/remove-from-subService")
    void removeSpecialistFromSubService(@RequestBody Map<String, Long> map) {
        subServiceSpecialistService.delete(map.get("specialistId"), map.get("subServiceId"));
    }
}
