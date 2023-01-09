package org.homeservice.repository.custom;

import org.homeservice.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepositoryCustom {
    public List<Order> findAllWithDetails(Map<String, String> filters);
}
