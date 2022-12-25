package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Customer;
import org.homeservice.entity.Order;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    private Double offerPrice;
    private String description;
    private LocalDateTime workingTime;
    private String address;
    private Long customerId;
    private Long subServiceId;

    public Order getOrder() {
        return new Order(offerPrice,description,workingTime,address);
    }
}
