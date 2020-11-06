package com.commerce.backend.api;


import com.commerce.backend.model.request.user.PasswordResetRequest;
import com.commerce.backend.model.request.user.UpdateUserAddressRequest;
import com.commerce.backend.model.request.user.UpdateUserRequest;
import com.commerce.backend.model.response.user.UserResponse;
import com.commerce.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebClient
@WithMockUser
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_get_user() throws Exception {

        // given
        UserResponse userResponse = new UserResponse();


        given(userService.fetchUser()).willReturn(userResponse);

        // when
        MvcResult result = mockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).fetchUser();
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    void it_should_update_user() throws Exception {

        // given
        String firstName = faker.address().firstName();
        String lastName = faker.address().lastName();
        String phone = faker.number().digits(12);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName(firstName);
        updateUserRequest.setLastName(lastName);
        updateUserRequest.setPhone(phone);

        UserResponse userResponse = new UserResponse();


        given(userService.updateUser(updateUserRequest)).willReturn(userResponse);

        // when
        MvcResult result = mockMvc.perform(put("/api/account")
                .content(objectMapper.writeValueAsString(updateUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).updateUser(updateUserRequest);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    void it_should_not_update_user_if_invalid_field() throws Exception {

        // given
        String firstName = faker.lorem().characters(2);
        String lastName = faker.address().lastName();
        String phone = faker.number().digits(12);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName(firstName);
        updateUserRequest.setLastName(lastName);
        updateUserRequest.setPhone(phone);

        // when
        MvcResult result = mockMvc.perform(put("/api/account")
                .content(objectMapper.writeValueAsString(updateUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        verify(userService, times(0)).updateUser(updateUserRequest);
        then(result.getResponse().getContentAsString()).contains("size must be between 3 and 26");
    }

    @Test
    void it_should_update_user_address() throws Exception {

        // given
        String city = faker.address().city();
        String state = faker.address().state();
        String zip = faker.number().digits(5);
        String country = faker.address().country();
        String address = faker.address().streetAddress();

        UpdateUserAddressRequest updateUserAddressRequest = new UpdateUserAddressRequest();
        updateUserAddressRequest.setCity(city);
        updateUserAddressRequest.setState(state);
        updateUserAddressRequest.setZip(zip);
        updateUserAddressRequest.setCountry(country);
        updateUserAddressRequest.setAddress(address);

        UserResponse userResponse = new UserResponse();


        given(userService.updateUserAddress(updateUserAddressRequest)).willReturn(userResponse);

        // when
        MvcResult result = mockMvc.perform(put("/api/account/address")
                .content(objectMapper.writeValueAsString(updateUserAddressRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).updateUserAddress(updateUserAddressRequest);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    void it_should_not_update_user_address_if_invalid_field() throws Exception {

        // given
        String city = faker.number().digits(2);
        String state = faker.address().state();
        String zip = faker.number().digits(5);
        String country = faker.address().country();
        String address = faker.address().streetAddress();

        UpdateUserAddressRequest updateUserAddressRequest = new UpdateUserAddressRequest();
        updateUserAddressRequest.setCity(city);
        updateUserAddressRequest.setState(state);
        updateUserAddressRequest.setZip(zip);
        updateUserAddressRequest.setCountry(country);
        updateUserAddressRequest.setAddress(address);

        // when
        MvcResult result = mockMvc.perform(put("/api/account/address")
                .content(objectMapper.writeValueAsString(updateUserAddressRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        verify(userService, times(0)).updateUserAddress(updateUserAddressRequest);
        then(result.getResponse().getContentAsString()).contains("size must be between 3 and 100");
    }

    @Test
    void it_should_reset_user_password() throws Exception {

        // given
        String oldPassword = faker.number().digits(6);
        String newPassword = faker.number().digits(6);
        String newPasswordConfirm = newPassword + "";

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setOldPassword(oldPassword);
        passwordResetRequest.setNewPassword(newPassword);
        passwordResetRequest.setNewPasswordConfirm(newPasswordConfirm);

        // when
        mockMvc.perform(post("/api/account/password/reset")
                .content(objectMapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).resetPassword(passwordResetRequest);
    }

    @Test
    void it_should_reset_user_password_if_invalid_field() throws Exception {

        // given
        String oldPassword = null;
        String newPassword = faker.number().digits(6);
        String newPasswordConfirm = newPassword + "";

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setOldPassword(oldPassword);
        passwordResetRequest.setNewPassword(newPassword);
        passwordResetRequest.setNewPasswordConfirm(newPasswordConfirm);

        // when
        MvcResult result = mockMvc.perform(post("/api/account/password/reset")
                .content(objectMapper.writeValueAsString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        verify(userService, times(0)).resetPassword(passwordResetRequest);
        then(result.getResponse().getContentAsString()).contains("must not be blank");
    }

    @Test
    void it_should_get_verification_status() throws Exception {

        // given
        Boolean status = faker.bool().bool();

        given(userService.getVerificationStatus()).willReturn(status);

        // when
        MvcResult result = mockMvc.perform(get("/api/account/status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).getVerificationStatus();
        then(result.getResponse().getContentAsString()).isEqualTo(status.toString());
    }


}