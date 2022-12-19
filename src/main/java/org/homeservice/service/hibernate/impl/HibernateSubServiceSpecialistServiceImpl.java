package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SubService;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.repository.hibernate.HibernateSubServiceSpecialistRepository;
import org.homeservice.repository.hibernate.impl.HibernateSubServiceSpecialistRepositoryImpl;
import org.homeservice.service.hibernate.HibernateSubServiceSpecialistService;
import org.homeservice.service.hibernate.base.HibernateBaseServiceImpl;

public class HibernateSubServiceSpecialistServiceImpl extends HibernateBaseServiceImpl<SubServiceSpecialist, SubServiceSpecialistId
        , HibernateSubServiceSpecialistRepository> implements HibernateSubServiceSpecialistService {
    private static HibernateSubServiceSpecialistService service;

    private HibernateSubServiceSpecialistServiceImpl() {
        super(HibernateSubServiceSpecialistRepositoryImpl.getRepository());
    }

    @Override
    public void save(Long specialistId, Long subServiceId) {
        SubServiceSpecialist subServiceSpecialist = initialSubServiceSpecialist(specialistId, subServiceId);
        super.save(subServiceSpecialist);
    }

    @Override
    public void remove(Long specialistId, Long subServiceId) {
        SubServiceSpecialist subServiceSpecialist = initialSubServiceSpecialist(specialistId, subServiceId);
        super.remove(subServiceSpecialist);
    }

    private SubServiceSpecialist initialSubServiceSpecialist(Long specialistId, Long subServiceId) {
        Specialist specialist = new Specialist(specialistId);
        SubService subService = new SubService(subServiceId);
        return new SubServiceSpecialist(specialist, subService);
    }

    public static HibernateSubServiceSpecialistService getService() {
        if (service == null)
            service = new HibernateSubServiceSpecialistServiceImpl();
        return service;
    }

}
