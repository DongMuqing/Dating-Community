package com.susu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Date:2023/8/20 20:42
 * @Created by Muqing
 */
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    @Value("${spring.mail.username}")
    private String username;
    public void sendVerificationCode(String to, String code) throws MessagingException {
        //返回内容为html
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom(username); // 设置发件人地址
        helper.setSubject("Verification Code");
        Context context = new Context();
        context.setVariable("verificationCode", code);
        String htmlContent = templateEngine.process("verification-email", context);
        helper.setText(htmlContent, true); // 设置为 true，表示内容为 HTML

        javaMailSender.send(message);
    }
}
