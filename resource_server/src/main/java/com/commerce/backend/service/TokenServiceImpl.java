package com.commerce.backend.service;

import com.commerce.backend.dao.PasswordForgotTokenRepository;
import com.commerce.backend.dao.VerificationTokenRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.entity.PasswordForgotToken;
import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.entity.VerificationToken;
import com.commerce.backend.model.event.OnPasswordForgotRequestEvent;
import com.commerce.backend.model.event.OnRegistrationCompleteEvent;
import com.commerce.backend.model.request.user.PasswordForgotValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final int EXPIRY_DATE = 60 * 24;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordForgotTokenRepository passwordForgotTokenRepository;

    @Autowired
    public TokenServiceImpl(UserService userService,
                            PasswordEncoder passwordEncoder,
                            ApplicationEventPublisher eventPublisher,
                            VerificationTokenRepository verificationTokenRepository,
                            PasswordForgotTokenRepository passwordForgotTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordForgotTokenRepository = passwordForgotTokenRepository;
    }

    @Override
    public void createEmailConfirmToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(calculateExpiryDate());
        verificationTokenRepository.save(verificationToken);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, token));
    }

    @Override
    public void createPasswordResetToken(String email) {
        User user = userService.findByEmail(email);
        if (Objects.isNull(user)) {
            return;
        }

        PasswordForgotToken passwordForgotToken = passwordForgotTokenRepository.findByUser(user)
                .orElse(null);
        if (Objects.isNull(passwordForgotToken)) {
            passwordForgotToken = new PasswordForgotToken();
            passwordForgotToken.setUser(user);
        }

        String token = UUID.randomUUID().toString();
        passwordForgotToken.setToken(token);
        passwordForgotToken.setExpiryDate(calculateExpiryDate());
        passwordForgotTokenRepository.save(passwordForgotToken);

        eventPublisher.publishEvent(new OnPasswordForgotRequestEvent(user, token));
    }

    @Override
    public void validateEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Null verification token"));


        User user = verificationToken.getUser();

        if (Objects.isNull(user)) {
            throw new ResourceNotFoundException("User not found");
        }

        checkTokenExpire(verificationToken.getExpiryDate());

        user.setEmailVerified(1);
        verificationTokenRepository.delete(verificationToken);
        userService.saveUser(user);
    }

    @Override
    public void validateForgotPasswordConfirm(String token) {
        PasswordForgotToken passwordForgotToken = passwordForgotTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        checkTokenExpire(passwordForgotToken.getExpiryDate());
    }

    @Override
    public void validateForgotPassword(PasswordForgotValidateRequest passwordForgotValidateRequest) {
        PasswordForgotToken passwordForgotToken = passwordForgotTokenRepository.findByToken(passwordForgotValidateRequest.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        User user = passwordForgotToken.getUser();

        if (Objects.isNull(user)) {
            throw new ResourceNotFoundException("User not found");
        }

        checkTokenExpire(passwordForgotToken.getExpiryDate());

        if (passwordEncoder.matches(passwordForgotValidateRequest.getNewPassword(), user.getPassword())) {
            return;
        }

        user.setPassword(passwordEncoder.encode(passwordForgotValidateRequest.getNewPassword()));
        userService.saveUser(user);
        passwordForgotTokenRepository.delete(passwordForgotToken);
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, TokenServiceImpl.EXPIRY_DATE);
        return new Date(cal.getTime().getTime());
    }

    private void checkTokenExpire(Date date) {
        if ((date.getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
            throw new InvalidArgumentException("Token is expired");
        }

    }
}
