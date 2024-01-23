package com.stockanalytics.accounting.emailservice;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fintechstock2023@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        this.mailSender.send(message);
    }
}
