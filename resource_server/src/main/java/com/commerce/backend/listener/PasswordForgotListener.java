package com.commerce.backend.listener;

import com.commerce.backend.constants.MailConstants;
import com.commerce.backend.model.event.OnPasswordForgotRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;



@Component
public class PasswordForgotListener implements ApplicationListener<OnPasswordForgotRequestEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstants mailConstants;

    @Override
    public void onApplicationEvent(OnPasswordForgotRequestEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnPasswordForgotRequestEvent event) {
        String recipientAddress = event.getUser().getEmail();
        String subject = "\uD83D\uDD11 Keyist Password Reset Confirmation";
        String confirmationUrl = mailConstants.getHostAddress() + "/passwordResetConfirm?token=" + event.getToken();
        String message = "Hi ,\n\nPlease reset your password with this link.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n\n" + confirmationUrl + "\n\n\nw/ Keyist Team");
        mailSender.send(email);
    }
}