package com.commerce.backend.event.listener;

import com.commerce.backend.dao.VerificationTokenRepository;
import com.commerce.backend.event.OnRegistrationCompleteEvent;
import com.commerce.backend.model.User;
import com.commerce.backend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {


    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(verificationToken.calculateExpiryDate(60 * 24));
        verificationTokenRepository.save(verificationToken);

        String recipientAddress = user.getEmail();
        String subject = "Keyist Registration Confirmation";
        String confirmationUrl = event.getAppUrl()+"/registrationConfirm?token=" + token;
        String message = "Hi ,\n\nPlease confirm your email with this link. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\nw/ Keyist Team");
        mailSender.send(email);
    }
}