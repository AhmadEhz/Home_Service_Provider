package org.homeservice.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.homeservice.util.exception.ConnectionBrokenException;
import org.springframework.stereotype.Component;


@Component
public class EmailSender {
    private static final String subject = "Verify your email";
    private static final String message =
            "To verify your email, please click on link below:\n http://localhost:8080/%s/verifyEmail?verify=%s";

    public void send(String to, String verificationCode, EmailFor emailFor) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post(
                    "https://api.mailgun.net/v3/sandbox6a29fe44516146a3827e305ab7524936.mailgun.org/messages").
                    basicAuth("api", "f09ad9181a7d5502d82502fd9c1ba18f-4c2b2223-4fce5800")
                    .queryString("from", "mailgun@sandbox6a29fe44516146a3827e305ab7524936.mailgun.org")
                    .queryString("to", to)
                    .queryString("subject", subject)
                    .queryString("text", String.format(message, emailFor.value, verificationCode))
                    .asJson();
        } catch (UnirestException e) {
            throw new ConnectionBrokenException("Can't send email verification. please try again.");
        }
    }

    public enum EmailFor {
        SPECIALIST("specialist"), CUSTOMER("customer");

        private final String value;

        EmailFor(String value) {
            this.value = value;
        }
    }
}
