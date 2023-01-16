package org.homeservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Bid;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidCreationDto {
    private Double offerPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startWorking;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endWorking;

    private String description;

    private Long orderId;

    @JsonIgnore
    public Bid getBid() {
        Bid bid = new Bid();
        bid.setOfferPrice(offerPrice);
        bid.setStartWorking(startWorking);
        bid.setEndWorking(endWorking);
        bid.setDescription(description);
        return bid;
    }
}
