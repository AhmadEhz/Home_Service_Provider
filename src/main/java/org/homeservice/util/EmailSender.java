package org.homeservice.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.homeservice.util.exception.ConnectionBrokenException;
//import org.homeservice.util.exception.CustomIllegalArgumentException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailSender {
    private static final String subject = "Verify your email";
    private static final String message =
            "To verify your email, please click on link below:\n http://localhost:8080/%s/verifyEmail?verify=%s";

//    private final JavaMailSender emailSender;
//
//    public EmailSender(JavaMailSender emailSender) {
//        this.emailSender = emailSender;
//    }
//
//    public void sendVerifyingEmail(String to, String verificationCode, EmailFor emailFor) {
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setFrom("noreply@gamil.com");
//        mail.setTo(to);
//        mail.setSubject(subject);
//        mail.setText(String.format(message, emailFor.value, verificationCode));
//        emailSender.send(mail);
//    }

    public void sendSimpleMessage(String to, String verificationCode, EmailFor emailFor) {
        try {
            Unirest.post("https://api.mailgun.net/v3/sandbox6a29fe44516146a3827e305ab7524936.mailgun.org"
                         + "/messages").
                    basicAuth("api", "f09ad9181a7d5502d82502fd9c1ba18f-4c2b2223-4fce5800")
                    .queryString("from", "sandbox6a29fe44516146a3827e305ab7524936.mailgun.org")
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
