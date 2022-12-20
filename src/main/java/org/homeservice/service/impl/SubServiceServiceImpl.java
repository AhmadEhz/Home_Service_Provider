package org.homeservice.service.impl;

import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.service.ServiceService;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;

import java.util.List;

@org.springframework.stereotype.Service
@Scope("singleton")
public class SubServiceServiceImpl extends BaseServiceImpl<SubService, Long, SubServiceRepository>
        implements SubServiceService {
    private final ServiceService serviceService;

    public SubServiceServiceImpl(SubServiceRepository repository, ServiceService serviceService) {
        super(repository);
        this.serviceService = serviceService;
    }

    @Override
    public void save(SubService subService, String serviceName) {
        Service service = serviceService.findByName(serviceName).orElseThrow(() -> new NotFoundException("Service not found."));
        subService.setService(service);
        save(subService);
    }

    @Override
    public void save(SubService subService) {
        if (subService.getService() == null || subService.getService().getId() == null)
            throw new NullPointerException("Service or serviceId is null");
        if(isExistedByNameAndServiceId(subService.getName(),subService.getService().getId()))
            throw new CustomIllegalArgumentException("This SubService is existed in this service.");

        super.save(subService);
    }

    @Override
    public List<SubService> loadAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist(specialistId);
    }

    @Override
    public List<SubService> loadAllByService(Long serviceId) {
        return repository.findSubServicesByService_Id(serviceId);
    }

    @Override
    public void editDescription(String newDescription, Long id) {
        SubService subService = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService not found."));
        subService.setDescription(newDescription);
        update(subService);
    }

    @Override
    public void editBasePrice(double basePrice, Long id) {
        SubService subService = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService not found."));
        subService.setBasePrice(basePrice);
        update(subService);
    }

    @Override
    public boolean isExistedByNameAndServiceId(String subServiceName, Long serviceId) {
        return repository.findSubServiceByNameAndService_Id(subServiceName, serviceId).isPresent();
    }
}
