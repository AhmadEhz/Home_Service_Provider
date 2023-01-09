package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BidDto {
    private Long id;
    private Double offerPrice;
    private LocalDateTime startWorking;
    private LocalDateTime endWorking;
    private SpecialistDto specialist;

    public BidDto() {
    }

    public BidDto(Bid bid) {
        id = bid.getId();
        offerPrice = bid.getOfferPrice();
        startWorking = bid.getStartWorking();
        endWorking = bid.getEndWorking();
        if (bid.getSpecialist() != null)
            specialist = new SpecialistDto(bid.getSpecialist());
    }

    public static List<BidDto> convertToDto(List<Bid> bids) {
        List<BidDto> bidDtoList = new ArrayList<>(bids.size());
        for (Bid b : bids) {
            bidDtoList.add(new BidDto(b));
        }
        return bidDtoList;
    }

}
