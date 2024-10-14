package com.olifarhaan.service.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${frontend.reset-password.url}")
    private String resetPasswordFrontendUrl;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Password Reset";
        String link = String.format("%s?token=%s", resetPasswordFrontendUrl, resetToken);
        String text = "Click the following link to reset your password: " + link;
        sendEmail(to, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true indicates HTML content

            javaMailSender.send(mimeMessage);
        } catch (Throwable e) {
            logger.error("Failed to send email to " + to, e);
        }
    }
}
