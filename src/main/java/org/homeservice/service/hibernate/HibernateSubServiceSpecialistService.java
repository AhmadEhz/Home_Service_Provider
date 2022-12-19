package org.homeservice.service.hibernate;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.service.hibernate.base.HibernateBaseService;

public interface HibernateSubServiceSpecialistService extends HibernateBaseService<SubServiceSpecialist, SubServiceSpecialistId> {
    void save(Long specialistId, Long subServiceId);

    void remove(Long specialistId, Long subServiceId);
}
