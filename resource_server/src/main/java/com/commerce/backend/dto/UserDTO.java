package com.commerce.backend.dto;

import com.commerce.backend.validator.CustomEmail;
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
public class UserDTO {

    @CustomEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 6)
    private String password;

    @NotNull
    @NotEmpty
    private String passwordRepeat;

}
