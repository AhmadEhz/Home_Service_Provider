package org.homeservice.controller;

import org.homeservice.dto.*;
import org.homeservice.entity.*;
import org.homeservice.service.*;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.security.core.Authentication;
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
    private final OrderService orderService;

    public AdminController(AdminService adminService, SpecialistService specialistService,
                           ServiceService serviceService, SubServiceService subServiceService,
                           SubServiceSpecialistService subServiceSpecialistService, CustomerService customerService, OrderService orderService) {
        this.adminService = adminService;
        this.specialistService = specialistService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
        this.subServiceSpecialistService = subServiceSpecialistService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping("/save")
    void save(@RequestBody Admin admin) {
        adminService.save(admin);
    }

    @PutMapping("/change-password")
    void changePassword(@RequestBody Map<String, String> map, Authentication user) {
        if (!map.containsKey("oldPassword") || !map.containsKey("newPassword"))
            throw new CustomIllegalArgumentException();
        adminService.changePassword((Admin) user.getPrincipal(), map.get("oldPassword"), map.get("newPassword"));
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
    void addSpecialistToSubService(@RequestParam Long specialistId, @RequestParam Long subServiceId) {
        subServiceSpecialistService.save(specialistId, subServiceId);
    }

    @PutMapping("specialist/remove-from-subService")
    void removeSpecialistFromSubService(@RequestBody Map<String, Long> map) {
        subServiceSpecialistService.delete(map.get("specialistId"), map.get("subServiceId"));
    }

    @GetMapping("/specialist/show")
    SpecialistDto loadSpecialist(@RequestParam Long specialistId) {
        Specialist specialist = specialistService.loadById(specialistId)
                .orElseThrow(() -> new NotFoundException("Specialist not found."));
        return new SpecialistDto(specialist);
    }

    @GetMapping("/customer/show")
    CustomerDto loadCustomer(@RequestParam Long customerId) {
        Customer customer = customerService.loadById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found."));
        return new CustomerDto(customer);
    }

    @GetMapping("/specialist/showAll")
    List<SpecialistDto> showSpecialists(@RequestParam Map<String, String> filters) {
        List<Specialist> specialists = specialistService.loadAllByFilter(filters);
        return SpecialistDto.convertToDto(specialists);
    }

    @GetMapping("/specialist/showAll-by-subService")
    List<SpecialistDto> showSpecialistsBySubService(@RequestParam Long subServiceId) {
        List<Specialist> specialists = specialistService.loadAllBySubService(subServiceId);
        return SpecialistDto.convertToDto(specialists);
    }

    @GetMapping("/customer/showAll")
    List<CustomerDto> showCustomers(@RequestParam Map<String, String> filters) {
        List<Customer> customers = customerService.loadAllByFilter(filters);
        return CustomerDto.convertToDto(customers);
    }

    @GetMapping("/show-service")
    ServiceDto showService(@RequestParam Long id) {
        Service service = serviceService.loadById(id).orElseThrow(() -> new NotFoundException("Service not found."));
        return new ServiceDto(service);
    }

    @GetMapping("/show-subService")
    SubServiceDto showSubService(@RequestParam Long id) {
        SubService subService = subServiceService.loadById(id).orElseThrow(() ->
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
        List<Service> services = serviceService.loadAll();
        return ServiceDto.convertToDto(services);
    }

    @GetMapping("/orders")
    List<OrderDto> showAllWithFilter(@RequestParam Map<String, String> filters) {
        List<Order> orders = orderService.loadAllWithDetails(filters);
        return OrderDto.convertToDto(orders);
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
