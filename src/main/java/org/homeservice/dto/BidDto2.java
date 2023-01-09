package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidDto2 {
    private Long id;
    private Double offerPrice;
    private LocalDateTime startWorking;
    private LocalDateTime endWorking;

    public BidDto2 () {

    }
    public BidDto2(Bid bid) {
        id = bid.getId();
        offerPrice = bid.getOfferPrice();
        startWorking = bid.getStartWorking();
        endWorking = bid.getEndWorking();
    }
}
