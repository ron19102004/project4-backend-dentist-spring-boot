package com.hospital.app.mailer;

import com.hospital.app.entities.account.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class MailerServiceImpl extends AbsMailerTemplateRoute implements MailerService {
    @Value("${mailer.username}")
    private String APPLICATION_EMAIL;
    @Value("${info.dentistName}")
    private String INFO_DENTIST_NAME;
    @Value("${info.hotline}")
    private String INFO_HOTLINE;
    @Value("${info.dentistEmail}")
    private String INFO_DENTIST_EMAIL;
    @Value("${app.url}")
    private String APP_URL;
    @Value("${mailInfoCustom.verifyResetPasswordFrontEndURL}")
    private String VERIFY_RESET_PW_FRONT_END_URL;
    private static final String ENCODING = "UTF-8";
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendOneTimePassword(User user, String otpCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            mimeMessageHelper.setSubject("Xác thực OTP");
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

    @Async
    @Override
    public void sendWelcomeEmail(User user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            mimeMessageHelper.setSubject("Đăng ký tài khoản thành công!");
            mimeMessageHelper.setFrom(APPLICATION_EMAIL);
            mimeMessageHelper.setTo(user.getEmail());

            Context context = new Context();
            context.setVariable("name", user.getFullName());
            context.setVariable("dentistName", INFO_DENTIST_NAME);
            context.setVariable("email", user.getEmail());
            context.setVariable("yearCurrent", LocalDateTime.now().getYear());
            context.setVariable("hotline", INFO_HOTLINE);
            context.setVariable("dentistEmail", INFO_DENTIST_EMAIL);

            String content = templateEngine.process(WELCOME_REGISTER_TEMPLATE, context);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            log.error(exception.getMessage());
        }
    }

    @Async
    @Override
    public void requestResetPassword(User user, String token, Map<String, Object> claims) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            mimeMessageHelper.setSubject("Khôi phục Mật Khẩu!");
            mimeMessageHelper.setFrom(APPLICATION_EMAIL);
            mimeMessageHelper.setTo(user.getEmail());

            String resetPasswordUrl = VERIFY_RESET_PW_FRONT_END_URL + "?token="+token;

            Context context = new Context();
            context.setVariable("account", user.getFullName());
            context.setVariable("password", claims.get("password"));
            context.setVariable("resetPasswordUrl", resetPasswordUrl);
            context.setVariable("hotline", INFO_HOTLINE);
            context.setVariable("dentistEmail", INFO_DENTIST_EMAIL);

            String content = templateEngine.process(RESET_PASSWORD_TEMPLATE, context);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            log.error(exception.getMessage());
        }
    }
}
