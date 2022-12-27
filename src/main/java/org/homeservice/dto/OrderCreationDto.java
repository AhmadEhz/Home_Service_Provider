package org.homeservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Order;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCreationDto {
    private Double offerPrice;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workingTime;
    private String address;
    private Long customerId;
    private Long subServiceId;

    public Order getOrder() {
        return new Order(offerPrice,description,workingTime,address);
    }
}
