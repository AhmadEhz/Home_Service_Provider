package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Order;
import org.homeservice.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Double offerPrice;
    private String description;
    private LocalDateTime workingTime;
    private String address;
    private Double finalPrice;
    private LocalDateTime startWorking;
    private LocalDateTime endWorking;
    private OrderStatus status;
    private BidDto2 bid;
    private Long timeSpentMinutes;
    private SubServiceDto subService;
    private SpecialistDto specialist;
    private CustomerDto customer;

    public OrderDto() {
    }

    public OrderDto(Order order) {
        id = order.getId();
        offerPrice = order.getCustomerOfferPrice();
        description = order.getDescription();
        workingTime = order.getWorkingTime();
        address = order.getAddress();
        finalPrice = order.getFinalPrice();
        startWorking = order.getStartWorking();
        endWorking = order.getEndWorking();
        status = order.getStatus();
        timeSpentMinutes = order.getTimeSpent() != null ? order.getTimeSpent().toMinutes() : null;
        subService = order.getSubService() != null ? new SubServiceDto(order.getSubService()) : null;
        specialist = order.getSpecialist() != null ? new SpecialistDto(order.getSpecialist()) : null;
        customer = order.getCustomer() != null ? new CustomerDto(order.getCustomer()) : null;
        bid = order.getAcceptedBid() != null ? new BidDto2(order.getAcceptedBid()) : null;
    }

    public static List<OrderDto> convertToDto(List<Order> orders) {
        List<OrderDto> orderDtoList = new ArrayList<>(orders.size());
        for (Order o : orders)
            orderDtoList.add(new OrderDto(o));
        return orderDtoList;
    }
}
