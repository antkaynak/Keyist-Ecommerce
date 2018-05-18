package com.commerce.backend.service;

import com.commerce.backend.dao.EmailResetTokenRepository;
import com.commerce.backend.dao.PasswordForgotTokenRepository;
import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.dao.VerificationTokenRepository;
import com.commerce.backend.dto.EmailResetDTO;
import com.commerce.backend.dto.PasswordForgotDTO;
import com.commerce.backend.event.OnEmailResetRequestEvent;
import com.commerce.backend.event.OnPasswordForgotRequestEvent;
import com.commerce.backend.event.OnRegistrationCompleteEvent;
import com.commerce.backend.model.EmailResetToken;
import com.commerce.backend.model.PasswordForgotToken;
import com.commerce.backend.model.User;
import com.commerce.backend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;

@Service
public class TokenServiceImpl implements TokenService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailResetTokenRepository emailResetTokenRepository;
    private final PasswordForgotTokenRepository passwordForgotTokenRepository;

    @Autowired
    public TokenServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, VerificationTokenRepository verificationTokenRepository, EmailResetTokenRepository emailResetTokenRepository, PasswordForgotTokenRepository passwordForgotTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailResetTokenRepository = emailResetTokenRepository;
        this.passwordForgotTokenRepository = passwordForgotTokenRepository;
    }

    @Override
    public void createEmailResetToken(Principal principal, EmailResetDTO emailResetDTO, String requestUrl) {
        User user = getUserFromPrinciple(principal);
        if (!passwordEncoder.matches(emailResetDTO.getPassword(), user.getPassword())
                || !emailResetDTO.getNewEmail().equals(emailResetDTO.getNewEmailConfirm())
                || user.getEmail().equals(emailResetDTO.getNewEmail())) {
            throw new IllegalArgumentException("Invalid password");
        }

        if (userRepository.existsByEmail(emailResetDTO.getNewEmail())) {
            throw new IllegalStateException("Email already exists");
        }
        eventPublisher.publishEvent(new OnEmailResetRequestEvent(user, requestUrl, emailResetDTO.getNewEmail()));
    }

    @Override
    public void createEmailConfirmToken(User user, String requestUrl) {
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, requestUrl));
    }

    @Override
    public void createPasswordResetToken(String email, String requestUrl) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return; //returning okay so we won't expose which email we have in the db
        }

        eventPublisher.publishEvent(new OnPasswordForgotRequestEvent(user, requestUrl));
    }

    @Override
    public void validateEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new IllegalArgumentException("Null verification token");
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new IllegalArgumentException("Token is expired");
        }

        user.setEmailVerified(1);
        verificationTokenRepository.delete(verificationToken);
        userRepository.save(user);
    }

    @Override
    public void validateEmailReset(String token) {
        EmailResetToken emailResetToken = emailResetTokenRepository.findByToken(token);
        if (emailResetToken == null) {
            throw new IllegalArgumentException("Null emailReset token");
        }

        User user = emailResetToken.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Calendar cal = Calendar.getInstance();
        if ((emailResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new IllegalArgumentException("Token is expired");
        }

        user.setEmailVerified(1);
        user.setEmail(emailResetToken.getEmail());
        emailResetTokenRepository.delete(emailResetToken);
        userRepository.save(user);
    }

    @Override
    public void validateForgotPasswordConfirm(String token) {
        PasswordForgotToken passwordForgotToken = passwordForgotTokenRepository.findByToken(token);
        if (passwordForgotToken == null) {
            throw new IllegalArgumentException("Token not found");
        }

        Calendar cal = Calendar.getInstance();
        if ((passwordForgotToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new IllegalArgumentException("Token is expired");
        }

    }

    @Override
    public void validateForgotPassword(PasswordForgotDTO passwordForgotDTO) {
        PasswordForgotToken passwordForgotToken = passwordForgotTokenRepository.findByToken(passwordForgotDTO.getToken());
        if (passwordForgotToken == null) {
            throw new IllegalArgumentException("Token not found");
        }

        User user = passwordForgotToken.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (passwordEncoder.matches(passwordForgotDTO.getNewPassword(), user.getPassword())) {
            return; //If the new password is the one user already has just return ok.
        }

        user.setPassword(passwordEncoder.encode(passwordForgotDTO.getNewPassword()));
        userRepository.save(user);
        passwordForgotTokenRepository.delete(passwordForgotToken);

    }

    private User getUserFromPrinciple(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("Invalid access");
        }
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }
}
