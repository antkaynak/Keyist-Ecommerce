package com.commerce.backend.listener;

import com.commerce.backend.constants.MailConstants;
import com.commerce.backend.model.event.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstants mailConstants;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        String recipientAddress = event.getUser().getEmail();
        String subject = "\uD83D\uDD11 Keyist Registration Confirmation";
        String confirmationUrl = mailConstants.getHostAddress() + "/registrationConfirm?token=" + event.getToken();
        String message = "Hi ,\n\nPlease confirm your email with this link. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\nw/ Keyist Team");
        mailSender.send(email);
    }
}