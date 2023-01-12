package org.homeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Rate;

@Getter
@Setter
public class RateDto {
    private Integer score;
    private String comment;
    private Long orderId;

    @JsonIgnore
    public Rate getRate() {
        return new Rate(score, comment);
    }
}
