package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.repository.hibernate.SubServiceSpecialistRepository;
import org.homeservice.repository.hibernate.base.BaseRepository;
import org.homeservice.repository.hibernate.impl.SubServiceSpecialistRepositoryImpl;
import org.homeservice.service.hibernate.SubServiceSpecialistService;
import org.homeservice.service.hibernate.base.BaseServiceImpl;

public class SubServiceSpecialistServiceImpl extends BaseServiceImpl<SubServiceSpecialist, SubServiceSpecialistId
        , SubServiceSpecialistRepository> implements SubServiceSpecialistService {
    private static SubServiceSpecialistService service;
    private SubServiceSpecialistServiceImpl() {
        super(SubServiceSpecialistRepositoryImpl.getRepository());
    }
    public static SubServiceSpecialistService getService() {
        if (service == null)
            service = new SubServiceSpecialistServiceImpl();
        return service;
    }

}
