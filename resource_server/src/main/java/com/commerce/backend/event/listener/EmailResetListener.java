package com.commerce.backend.event.listener;

import com.commerce.backend.dao.EmailResetTokenRepository;
import com.commerce.backend.event.OnEmailResetRequestEvent;
import com.commerce.backend.model.EmailResetToken;
import com.commerce.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailResetListener implements
        ApplicationListener<OnEmailResetRequestEvent> {


    @Autowired
    private EmailResetTokenRepository emailResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void onApplicationEvent(OnEmailResetRequestEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnEmailResetRequestEvent event) {
        User user = event.getUser();
        EmailResetToken emailResetToken = emailResetTokenRepository.findByUser(user);
        if (emailResetToken == null) {
            emailResetToken = new EmailResetToken();
            emailResetToken.setUser(user);
        }
        String token = UUID.randomUUID().toString();
        emailResetToken.setToken(token);
        emailResetToken.setEmail(event.getNewEmail());
        emailResetToken.setExpiryDate(emailResetToken.calculateExpiryDate(60 * 24));
        emailResetTokenRepository.save(emailResetToken);


        String recipientAddress = event.getNewEmail();
        String subject = "Keyist Email Reset Confirmation";
        String confirmationUrl = event.getAppUrl()+"/emailResetConfirm?token=" + emailResetToken.getToken();
        String message = "Hi ,\n\nPlease confirm your new e-mail with this link.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\nw/ Keyist Team");
        mailSender.send(email);
    }
}