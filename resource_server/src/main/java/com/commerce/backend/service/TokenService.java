package com.commerce.backend.service;

import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.user.PasswordForgotValidateRequest;


public interface TokenService {

    void createEmailConfirmToken(User user);

    void createPasswordResetToken(String email);

    void validateEmail(String token);

    void validateForgotPasswordConfirm(String token);

    void validateForgotPassword(PasswordForgotValidateRequest passwordForgotValidateRequest);
}
