package com.commerce.backend.event.listener;

import com.commerce.backend.dao.PasswordForgotTokenRepository;
import com.commerce.backend.event.OnPasswordForgotRequestEvent;
import com.commerce.backend.model.PasswordForgotToken;
import com.commerce.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordForgotListener implements
        ApplicationListener<OnPasswordForgotRequestEvent> {


    @Autowired
    private PasswordForgotTokenRepository passwordForgotTokenRepository;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void onApplicationEvent(OnPasswordForgotRequestEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnPasswordForgotRequestEvent event) {
        User user = event.getUser();
        PasswordForgotToken passwordForgotToken = passwordForgotTokenRepository.findByUser(event.getUser());
        if (passwordForgotToken == null) {
            passwordForgotToken = new PasswordForgotToken();
            passwordForgotToken.setUser(user);
        }
        String token = UUID.randomUUID().toString();
        passwordForgotToken.setToken(token);
        passwordForgotToken.setExpiryDate(passwordForgotToken.calculateExpiryDate(60 * 24));
        passwordForgotTokenRepository.save(passwordForgotToken);

        String recipientAddress = event.getUser().getEmail();
        String subject = "Keyist Password Reset Confirmation";
        String confirmationUrl = event.getAppUrl()+"/passwordResetConfirm?token=" + passwordForgotToken.getToken();
        String message = "Hi ,\n\nPlease reset your password with this link.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\nw/ Keyist Team");
        mailSender.send(email);
    }
}