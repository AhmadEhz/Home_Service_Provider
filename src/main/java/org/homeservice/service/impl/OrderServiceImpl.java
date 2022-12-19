package org.homeservice.service.impl;

import org.homeservice.entity.Order;
import org.homeservice.entity.OrderStatus;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.OrderService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.QueryUtil;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope("singleton")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }

    @Override
    public List<Order> findAllByCustomer(Long customerId) {
        return repository.findAllByCustomer_Id(customerId);
    }

    @Override
    public List<Order> findAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist_Id(specialistId);
    }

    @Override
    @Transactional
    public void changeStatusToChooseSpecialist(Long id) {
        int update = repository.changeStatus(id, OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST);
        QueryUtil.checkUpdate(update, () -> new NotFoundException("Order not found."));
    }
}
