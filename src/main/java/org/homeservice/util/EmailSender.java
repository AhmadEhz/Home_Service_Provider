package org.homeservice.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private static final String url = "http://localhost:8080/";
    private static final String param = "/verifyEmail?verify=";
    private static final String subject = "Verify your email";
    private static final String message = "To verify your email, please click on link below:\n";

    private final JavaMailSender emailSender;

    public EmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendVerifyingEmail(String to, String verificationCode, EmailFor emailFor) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("noreply@localhost");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message + url + emailFor.value + param + verificationCode);
        emailSender.send(mail);
    }

    public enum EmailFor {
        SPECIALIST("specialist"), CUSTOMER("customer");

        private final String value;

        EmailFor(String value) {
            this.value = value;
        }
    }
}
