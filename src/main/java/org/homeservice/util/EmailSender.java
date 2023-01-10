package org.homeservice.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    private static final String subject = "Verify your email";
    private static final String message = "To verify your email, please click on link below:";

    private final JavaMailSender emailSender;

    public EmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendVerifyingEmail(String to, String linkUrl) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("noreply@baeldung.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message + "\n" + linkUrl);
        emailSender.send(mail);
    }
}
