package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SubService;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.repository.hibernate.SubServiceSpecialistRepository;
import org.homeservice.repository.hibernate.impl.SubServiceSpecialistRepositoryImpl;
import org.homeservice.service.hibernate.SubServiceSpecialistService;
import org.homeservice.service.hibernate.base.BaseServiceImpl;

public class SubServiceSpecialistServiceImpl extends BaseServiceImpl<SubServiceSpecialist, SubServiceSpecialistId
        , SubServiceSpecialistRepository> implements SubServiceSpecialistService {
    private static SubServiceSpecialistService service;
    private SubServiceSpecialistServiceImpl() {
        super(SubServiceSpecialistRepositoryImpl.getRepository());
    }

    @Override
    public void save(Long specialistId, Long subServiceId) {
        Specialist specialist = new Specialist();
        SubService subService = new SubService();
        specialist.setId(specialistId);
        subService.setId(subServiceId);
        SubServiceSpecialist subServiceSpecialist = new SubServiceSpecialist(specialist,subService);
        super.save(subServiceSpecialist);
    }

    @Override
    public void delete(Long specialistId, Long subServiceId) {
        Specialist specialist = new Specialist();
        SubService subService = new SubService();
        specialist.setId(specialistId);
        subService.setId(subServiceId);
        SubServiceSpecialist subServiceSpecialist = new SubServiceSpecialist(specialist,subService);
        super.remove(subServiceSpecialist);
    }

    public static SubServiceSpecialistService getService() {
        if (service == null)
            service = new SubServiceSpecialistServiceImpl();
        return service;
    }

}
