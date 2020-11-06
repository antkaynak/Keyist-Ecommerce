package com.commerce.backend.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordForgotConfirmRequest {
    @NotBlank
    String token;
}
