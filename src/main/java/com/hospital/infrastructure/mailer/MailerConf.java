package com.hospital.infrastructure.mailer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailerConf {
    @Value("${mailer.host}")
    private String host;
    @Value("${mailer.port}")
    private int port;
    @Value("${mailer.username}")
    private String username;
    @Value("${mailer.password}")
    private String password;
    @Value("${mailer.properties.mail.smtp.auth}")
    private boolean auth;
    @Value("${mailer.properties.mail.smtp.starttls.enable}")
    private boolean starttls_enable;
    @Value("${mailer.properties.mail.debug}")
    private boolean debug;

    /**
     * Configuration mail sender
     *
     * @return JavaMailSender
     */
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls_enable);
        props.put("mail.debug", debug);

        return mailSender;
    }
}
