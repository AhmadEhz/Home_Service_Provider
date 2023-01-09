package org.homeservice.util;

import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

public class CaptchaChecker {
    private static final String url = "https://www.google.com/recaptcha/api/siteverify";
    private static final String secretKey = "6LfFGuQjAAAAANEUEHY3NLAeee9uU89qoVq2isiQ";
    private final String params;

    public CaptchaChecker(String captchaValue) {
        this.params = "?secret=" + secretKey + "&response=" + captchaValue;
    }

    public Boolean isValid() {
        String completeUrl = url + params;
        CaptchaResponse response = WebClient.create()
                .get()
                .uri(completeUrl)
                .retrieve()
                .bodyToMono(CaptchaResponse.class)
                .block();
        return response.success;
    }
}

@Data
class CaptchaResponse {
    Boolean success;
    String challenge_ts;
    String hostname;
}
