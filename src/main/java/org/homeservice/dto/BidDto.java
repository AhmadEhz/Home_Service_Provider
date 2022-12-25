package org.homeservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidDto {
    private Double offerPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startWorking;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endWorking;

    private Long orderId;

    private Long specialistId;

    public BidDto() {
    }

    public BidDto(Bid bid) {
        offerPrice = bid.getOfferPrice();
        startWorking = bid.getStartWorking();
        endWorking = bid.getEndWorking();
        if (bid.getOrder() != null)
            orderId = bid.getOrder().getId();
        if(bid.getSpecialist()!=null)
            specialistId = bid.getSpecialist().getId();
    }

    public Bid getBid() {
        Bid bid = new Bid();
        bid.setOfferPrice(offerPrice);
        bid.setStartWorking(startWorking);
        bid.setEndWorking(endWorking);
        return bid;
    }
}
