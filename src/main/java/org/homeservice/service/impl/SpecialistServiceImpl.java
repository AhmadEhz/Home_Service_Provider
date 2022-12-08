package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.impl.SpecialistRepositoryImpl;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;
import org.homeservice.util.exception.*;

import java.util.List;
import java.util.Optional;

public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    private static SpecialistService service;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    private SpecialistServiceImpl() {
        super(SpecialistRepositoryImpl.getRepository());
        serviceService = ServiceServiceImpl.getService();
        subServiceService = SubServiceServiceImpl.getService();
    }

    //todo: fill exception messages.
    @Override
    public List<Specialist> loadNewSpecialists() {
        return repository.findAll(SpecialistStatus.NEW);
    }

    @Override
    public List<Specialist> loadVerifiedSpecialists() {
        return repository.findAll(SpecialistStatus.ACCEPTED);
    }

    @Override
    public void changeStatus(Long specialistId, SpecialistStatus status) {
        int update = repository.changeStatus(specialistId, status);
        if (update < 1)
            throw new CustomIllegalArgumentException("Specialist with this id not found.");
    }



    @Override
    public int updateScore(Long id) {
        String query = """
                update Specialist set score =
                (select avg(r.score) from Rate as r where r.order.specialist.id=:id)
                where id = :id""";
        return HibernateUtil.getCurrentEntityManager()
                .createQuery(query).setParameter("id", id).executeUpdate();
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
