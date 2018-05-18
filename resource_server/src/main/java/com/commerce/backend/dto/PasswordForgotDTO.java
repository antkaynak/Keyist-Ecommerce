package com.commerce.backend.dto;

import com.commerce.backend.validator.PasswordMatches;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@PasswordMatches
public class PasswordForgotDTO {

    @NotNull
    @NotEmpty
    private String token;

    @NotNull
    @NotEmpty
    private String newPassword;

    @NotNull
    @NotEmpty
    private String newPasswordConfirm;

}
