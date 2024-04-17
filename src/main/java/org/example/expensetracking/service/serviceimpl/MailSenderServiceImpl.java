package org.example.expensetracking.service.serviceimpl;

import jakarta.mail.internet.MimeMessage;
import org.example.expensetracking.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private JavaMailSender javaMailSender;
    private SpringTemplateEngine templateEngine;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String toEmail, String otp) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("sokheng.tey03@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Your OTP for Email Verification");

            // Prepare the model
            Context context = new Context();
            context.setVariable("otp", otp);

            // Process the template
            String html = templateEngine.process("email/otp-email", context);

            // Attach the logo
            ClassPathResource logo = new ClassPathResource("static/images/spring-logo.png");
            helper.addInline("logo", logo);

            // Set the email content
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }
    }
}
