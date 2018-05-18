package com.commerce.backend.service;

import com.commerce.backend.dto.EmailResetDTO;
import com.commerce.backend.dto.PasswordForgotDTO;
import com.commerce.backend.model.User;

import java.security.Principal;

public interface TokenService {
    void createEmailResetToken(Principal principal, EmailResetDTO emailResetDTO, String requestUrl);

    void createEmailConfirmToken(User user, String requestUrl);

    void createPasswordResetToken(String email, String requestUrl);

    void validateEmail(String token);

    void validateEmailReset(String token);

    void validateForgotPasswordConfirm(String token);

    void validateForgotPassword(PasswordForgotDTO passwordForgotDTO);
}
