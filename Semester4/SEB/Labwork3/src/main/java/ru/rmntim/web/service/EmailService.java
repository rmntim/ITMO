package ru.rmntim.web.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.ContextResolver;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.util.MessageUtil;

import java.util.Properties;

@Stateless
@Slf4j
public class EmailService {
    private Session emailSession;

    @Resource(lookup = "java:global/mail/host")
    private String SMTP_HOST;

    @Resource(lookup = "java:global/mail/port")
    private String SMTP_PORT;

    @Resource(lookup = "java:global/mail/password")
    private String SMTP_PASSWORD;

    @Inject
    private MessageUtil messageUtil;

    @Context
    private ContextResolver<String> localeResolver;

    @PostConstruct
    public void init() {
        var props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        emailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("API_KEY", SMTP_PASSWORD);
            }
        });
    }

    public void sendEmail(String email, String subject, String text) {
        try {
            var fromAddress = "noreply@mail.rmntim.ru";

            var message = new MimeMessage(emailSession);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void sendSignUpEmail(String email, String username) {
        String language = localeResolver.getContext(String.class);
        var subject = messageUtil.getMessage("user.email.signup.subject", language);
        var text = messageUtil.getMessage("user.email.signup.content", language, username);

        sendEmail(email, subject, text);
    }

    public void sendPasswordChangeEmail(String email) {
        String language = localeResolver.getContext(String.class);
        var subject = messageUtil.getMessage("user.email.password.subject", language);
        var text = messageUtil.getMessage("user.email.password.content", language);

        sendEmail(email, subject, text);
    }
}
