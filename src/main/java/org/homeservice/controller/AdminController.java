package org.homeservice.controller;

import org.homeservice.dto.ServiceCreationDto;
import org.homeservice.dto.SpecialistDto;
import org.homeservice.dto.SubServiceDto;
import org.homeservice.entity.Admin;
import org.homeservice.entity.Customer;
import org.homeservice.entity.Specialist;
import org.homeservice.service.*;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final SpecialistService specialistService;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;
    private final SubServiceSpecialistService subServiceSpecialistService;
    private final CustomerService customerService;

    public AdminController(AdminService adminService, SpecialistService specialistService,
                           ServiceService serviceService, SubServiceService subServiceService,
                           SubServiceSpecialistService subServiceSpecialistService, CustomerService customerService) {
        this.adminService = adminService;
        this.specialistService = specialistService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
        this.subServiceSpecialistService = subServiceSpecialistService;
        this.customerService = customerService;
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
    void saveService(@RequestBody ServiceCreationDto serviceDto) {
        serviceService.save(serviceDto.getService());
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

    @GetMapping("/specialist/show")
    SpecialistDto loadSpecialist(@RequestParam Long specialistId) {
        Specialist specialist = specialistService.findById(specialistId)
                .orElseThrow(() -> new NotFoundException("Specialist not found."));
        return new SpecialistDto(specialist);
    }

    @GetMapping("/specialist/showAll")
    List<SpecialistDto> loadSpecialists(@RequestParam Map<String, String> filters) {
        List<Specialist> specialists = specialistService.loadAllByFilter(filters);
        return SpecialistDto.convertToDto(specialists);
    }

    @DeleteMapping("/specialist/delete")
    void deleteSpecialist(@RequestBody SpecialistDto specialistDto) {
        specialistService.deleteByAdmin(specialistDto.getSpecialist());
    }

    @DeleteMapping("/customer/delete")
    void deleteCustomer(@RequestBody Customer customer) {
        customerService.delete(customer);
    }
}
