package org.homeservice.service;

import org.homeservice.entity.SubService;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubServiceService extends BaseService<SubService, Long> {
    void save(SubService subService, String serviceName);

    List<SubService> loadAllBySpecialist(Long specialistId);

    List<SubService> loadAllByService(Long serviceId);

    void editDescription(String newDescription, Long id);

    void editBasePrice(double basePrice, Long id);

    boolean isExistedByNameAndServiceId(String subServiceName, Long serviceId);
}
