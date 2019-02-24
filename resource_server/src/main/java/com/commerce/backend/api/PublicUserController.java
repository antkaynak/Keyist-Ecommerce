package com.commerce.backend.api;


import com.commerce.backend.dto.PasswordForgotDTO;
import com.commerce.backend.dto.UserDTO;
import com.commerce.backend.model.User;
import com.commerce.backend.service.TokenService;
import com.commerce.backend.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class PublicUserController extends ApiController {


    private final UserService userService;


    private final TokenService tokenService;

    @Autowired
    public PublicUserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/account/registration", method = RequestMethod.POST)
    public ResponseEntity registerUserAccount(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = userService.register(userDTO);

        String appUrl = request.getRemoteHost();
        String appPort = String.valueOf(request.getServerPort());
        if (!appPort.trim().equals("")) {
            appUrl = appUrl + ":" + appPort;
        }

        tokenService.createEmailConfirmToken(user, appUrl);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/registration", method = RequestMethod.GET, params = "token")
    public ResponseEntity validateEmail(@RequestParam("token") String token) {
        tokenService.validateEmail(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/email/reset", method = RequestMethod.GET, params = "token")
    public ResponseEntity confirmEmailReset(@RequestParam("token") String token) {
        tokenService.validateEmailReset(token);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/account/password/forgot", method = RequestMethod.POST)
    public ResponseEntity forgotPasswordRequest(@RequestBody String payload, HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(payload);
        JsonNode jsonNode1 = actualObj.get("email");
        String email = jsonNode1.textValue();

        String appUrl = request.getRemoteHost();
        String appPort = String.valueOf(request.getServerPort());
        if (!appPort.trim().equals("")) {
            appUrl = appUrl + ":" + appPort;
        }

        tokenService.createPasswordResetToken(email, appUrl);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/account/password/forgot", method = RequestMethod.PUT)
    public ResponseEntity forgotPasswordPut(@RequestBody @Valid PasswordForgotDTO passwordForgotDTO, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            System.out.println("eee");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tokenService.validateForgotPassword(passwordForgotDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/password/forgot", method = RequestMethod.GET, params = "token")
    public ResponseEntity forgotPasswordConfirm(@RequestParam("token") String token) {
        tokenService.validateForgotPasswordConfirm(token);
        return new ResponseEntity(HttpStatus.OK);

    }

}
