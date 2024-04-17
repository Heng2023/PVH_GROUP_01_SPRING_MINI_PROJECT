package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.service.MailSenderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private JavaMailSender javaMailSender;

    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String toEmail, String otp ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("retsokhim2001@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }
}
