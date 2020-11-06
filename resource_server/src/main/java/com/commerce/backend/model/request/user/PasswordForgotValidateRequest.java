package com.commerce.backend.model.request.user;

import com.commerce.backend.validator.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class PasswordForgotValidateRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6, max = 52)
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;
}
