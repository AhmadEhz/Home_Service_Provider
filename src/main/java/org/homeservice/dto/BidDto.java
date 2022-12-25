package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BidDto {
    private Long id;
    private Double offerPrice;
    private LocalDateTime startWorking;
    private LocalDateTime endWorking;

    public BidDto() {
    }

    public BidDto(Bid bid) {
        id = bid.getId();
        offerPrice = bid.getOfferPrice();
        startWorking = bid.getStartWorking();
        endWorking = bid.getEndWorking();
    }
}
