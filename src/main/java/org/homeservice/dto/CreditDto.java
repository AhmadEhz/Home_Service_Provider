package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Credit;

@Getter
@Setter
public class CreditDto {
    private Long id;
    private Long amount;
    public CreditDto() {}
    public CreditDto(Credit credit) {
        id = credit.getId();
        amount = credit.getAmount();
    }
}
