package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Rate;

@Getter
@Setter
public class RateDto {
    private double score;
    private String comment;
    private Long orderId;
    private Long customerId;

    public Rate getRate() {
        return new Rate(score, comment);
    }
}
