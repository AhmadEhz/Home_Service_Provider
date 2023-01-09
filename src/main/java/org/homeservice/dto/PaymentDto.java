package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private Long cardNumber;
    private Integer cvv2;
    private String expiredDate;
    private String captcha;
}
