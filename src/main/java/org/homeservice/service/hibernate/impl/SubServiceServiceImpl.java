package org.homeservice.service.hibernate.impl;

import lombok.NonNull;
import org.homeservice.entity.SubService;
import org.homeservice.repository.hibernate.SubServiceRepository;
import org.homeservice.repository.hibernate.impl.SubServiceRepositoryImpl;
import org.homeservice.service.hibernate.SubServiceService;
import org.homeservice.service.hibernate.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;

import java.util.List;

public class SubServiceServiceImpl extends BaseServiceImpl<SubService, Long, SubServiceRepository>
        implements SubServiceService {
    private static SubServiceService service;

    private SubServiceServiceImpl() {
        super(SubServiceRepositoryImpl.getRepository());
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

    public static SubServiceService getService() {
        if (service == null)
            service = new SubServiceServiceImpl();
        return service;
    }
}
