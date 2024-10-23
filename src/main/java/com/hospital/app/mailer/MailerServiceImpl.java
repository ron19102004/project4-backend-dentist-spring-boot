package com.hospital.app.mailer;

import com.hospital.app.entities.account.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;

@Slf4j
@Service
public class MailerServiceImpl extends AbsMailerTemplateRoute implements MailerService {
    @Value("${mailer.username}")
    private String APPLICATION_EMAIL;
    private static final String ENCODING = "UTF-8";
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendOneTimePassword(User user, String otpCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            mimeMessageHelper.setSubject("Authentication OTP");
            mimeMessageHelper.setFrom(APPLICATION_EMAIL);
            mimeMessageHelper.setTo("user-email");

            Context context = new Context();
            context.setVariable("otp", otpCode);
            context.setVariable("name", user.getUsername());
            context.setVariable("time_sent", Instant.now());
            String content = templateEngine.process(ONE_TIME_PASSWORD_TEMPLATE, context);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            log.error(exception.getMessage());
        }
    }
}
