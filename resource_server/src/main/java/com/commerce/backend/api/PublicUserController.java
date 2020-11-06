package com.commerce.backend.api;


import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.user.*;
import com.commerce.backend.service.TokenService;
import com.commerce.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PublicUserController extends PublicApiController {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public PublicUserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/account/registration")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        User user = userService.register(registerUserRequest);
        tokenService.createEmailConfirmToken(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/registration/validate")
    public ResponseEntity<HttpStatus> validateEmail(@RequestBody @Valid ValidateEmailRequest validateEmailRequest) {
        tokenService.validateEmail(validateEmailRequest.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/forgot")
    public ResponseEntity<HttpStatus> forgotPasswordRequest(@RequestBody @Valid PasswordForgotRequest passwordForgotRequest) {
        tokenService.createPasswordResetToken(passwordForgotRequest.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/forgot/validate")
    public ResponseEntity<HttpStatus> validateForgotPassword(@RequestBody @Valid PasswordForgotValidateRequest passwordForgotValidateRequest) {
        tokenService.validateForgotPassword(passwordForgotValidateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/forgot/confirm")
    public ResponseEntity<HttpStatus> confirmForgotPassword(@RequestBody @Valid PasswordForgotConfirmRequest passwordForgotConfirmRequest) {
        tokenService.validateForgotPasswordConfirm(passwordForgotConfirmRequest.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
