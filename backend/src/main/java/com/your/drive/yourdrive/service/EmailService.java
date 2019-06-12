package com.your.drive.yourdrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;


@Component
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    public void sendEmail(String email, String message, String subject) throws Exception{
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setTo(email);
        helper.setText(message);
        helper.setSubject(subject);

        sender.send(mimeMessage);
    }
}
