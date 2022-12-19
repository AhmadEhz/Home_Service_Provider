package org.homeservice.service.hibernate.impl;

import lombok.NonNull;
import org.homeservice.entity.SubService;
import org.homeservice.repository.hibernate.HibernateSubServiceRepository;
import org.homeservice.repository.hibernate.impl.HibernateSubServiceRepositoryImpl;
import org.homeservice.service.hibernate.HibernateSubServiceService;
import org.homeservice.service.hibernate.base.HibernateBaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;

import java.util.List;

public class HibernateSubServiceServiceImpl extends HibernateBaseServiceImpl<SubService, Long, HibernateSubServiceRepository>
        implements HibernateSubServiceService {
    private static HibernateSubServiceService service;

    private HibernateSubServiceServiceImpl() {
        super(HibernateSubServiceRepositoryImpl.getRepository());
    }

    @Override
    public List<SubService> loadAll(Long serviceId) {
        return repository.findAll(serviceId);
    }

    @Override
    public void editDescription(@NonNull String newDescription, @NonNull Long id) {
        int update = repository.updateDescription(newDescription, id);
        if (update < 1) {
            throw new CustomIllegalArgumentException
                    ("SubService with this id is not exist.");
        }
    }

    @Override
    public void editBasePrice(double price, @NonNull Long id) {
        int update = repository.updateBasePrice(price, id);
        if (update < 1) {
            throw new CustomIllegalArgumentException
                    ("SubService with this id is not exist.");
        }
    }

    public static HibernateSubServiceService getService() {
        if (service == null)
            service = new HibernateSubServiceServiceImpl();
        return service;
    }
}
