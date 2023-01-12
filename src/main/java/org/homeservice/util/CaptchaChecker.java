package org.homeservice.util;

import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

public class CaptchaChecker {
    private static final String url = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private static final String secretKey = "6LfFGuQjAAAAANEUEHY3NLAeee9uU89qoVq2isiQ";
    private final String requestUrl;

    public CaptchaChecker(String captchaValue) {
        requestUrl = String.format(url, secretKey, captchaValue);
    }

    public Boolean isValid() {
        CaptchaResponse response = WebClient.create()
                .get()
                .uri(requestUrl)
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
