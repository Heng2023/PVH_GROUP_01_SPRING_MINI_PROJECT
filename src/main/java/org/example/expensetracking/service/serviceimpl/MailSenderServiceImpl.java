package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.service.MailSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;

    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String toEmail, String otp ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }
}
