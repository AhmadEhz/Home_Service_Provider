package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;
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
    private List<BidDto> bidDtoList;
    private SubServiceDto subServiceDto;

    public OrderDto() {
        bidDtoList = new ArrayList<>();
    }

    public OrderDto(Order order) {
        this();
        id = order.getId();
        offerPrice = order.getCustomerOfferPrice();
        description = order.getDescription();
        workingTime = order.getWorkingTime();
        address = order.getAddress();
        finalPrice = order.getFinalPrice();
        startWorking = order.getStartWorking();
        endWorking = order.getEndWorking();
        setBids(new ArrayList<>(order.getBids()));
    }

    public void setBids(List<Bid> bids) {
        if (bids == null)
            return;
        for (Bid b : bids)
            bidDtoList.add(new BidDto(b));
    }

    public static List<OrderDto> convertToDto(List<Order> orders) {
        List<OrderDto> orderDtoList = new ArrayList<>(orders.size());
        for (Order o : orders)
            orderDtoList.add(new OrderDto(o));
        return orderDtoList;
    }
}
