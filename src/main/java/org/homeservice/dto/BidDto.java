package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidDto {
    private Long id;
    private Double offerPrice;
    private LocalDateTime startWorking;
    private LocalDateTime endWorking;
    private SpecialistDto specialistDto;


    public BidDto() {
    }

    public BidDto(Bid bid) {
        id = bid.getId();
        offerPrice = bid.getOfferPrice();
        startWorking = bid.getStartWorking();
        endWorking = bid.getEndWorking();
        if (bid.getSpecialist() != null)
            specialistDto = new SpecialistDto(bid.getSpecialist());
    }
}
