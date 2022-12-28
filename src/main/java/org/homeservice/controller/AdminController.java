package org.homeservice.controller;

import org.homeservice.dto.*;
import org.homeservice.entity.*;
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

    @PutMapping("subservice/edit-description")
    void editSubServiceDescription(@RequestBody SubServiceEditionDto subServiceDto) {
        subServiceService.editDescription(subServiceDto.getDescription(), subServiceDto.getId());
    }

    @PutMapping("subservice/edit-basePrice")
    void editSubServiceBasePrice(@RequestBody SubServiceEditionDto subServiceDto) {
        subServiceService.editBasePrice(subServiceDto.getBasePrice(), subServiceDto.getId());
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

    @GetMapping("/customer/show")
    CustomerDto loadCustomer(@RequestParam Long customerId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found."));
        return new CustomerDto(customer);
    }

    @GetMapping("/specialist/showAll")
    List<SpecialistDto> loadSpecialists(@RequestParam Map<String, String> filters) {
        List<Specialist> specialists = specialistService.loadAllByFilter(filters);
        return SpecialistDto.convertToDto(specialists);
    }

    @GetMapping("/customer/showAll")
    List<CustomerDto> loadCustomers(@RequestParam Map<String, String> filters) {
        List<Customer> customers = customerService.loadAllByFilter(filters);
        return CustomerDto.convertToDto(customers);
    }

    @GetMapping("/show-service")
    ServiceDto showService(@RequestParam Long id) {
        Service service = serviceService.findById(id).orElseThrow(() -> new NotFoundException("Service not found."));
        return new ServiceDto(service);
    }

    @GetMapping("/show-subService")
    SubServiceDto showSubService(@RequestParam Long id) {
        SubService subService = subServiceService.findById(id).orElseThrow(() ->
                new NotFoundException("SubService not found."));
        return new SubServiceDto(subService);
    }

    @GetMapping("/show-subServices")
    List<SubServiceDto> showSubServices(@RequestParam Long serviceId) {
        List<SubService> subServices = subServiceService.loadAllByService(serviceId);
        return SubServiceDto.convertToDto(subServices);
    }

    @GetMapping("/show-services")
    List<ServiceDto> showServices() {
        List<Service> services = serviceService.findAll();
        return ServiceDto.convertToDto(services);
    }

    @DeleteMapping("/specialist/delete")
    void deleteSpecialist(@RequestBody SpecialistDto specialistDto) {
        specialistService.deleteByAdmin(specialistDto.getSpecialist());
    }

    @DeleteMapping("/customer/delete")
    void deleteCustomer(@RequestBody CustomerDto customerDto) {
        customerService.delete(customerDto.getCustomer());
    }
}
