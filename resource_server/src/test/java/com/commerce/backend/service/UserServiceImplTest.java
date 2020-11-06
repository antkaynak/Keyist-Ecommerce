package com.commerce.backend.service;

import com.commerce.backend.converter.user.UserResponseConverter;
import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.user.PasswordResetRequest;
import com.commerce.backend.model.request.user.RegisterUserRequest;
import com.commerce.backend.model.request.user.UpdateUserAddressRequest;
import com.commerce.backend.model.request.user.UpdateUserRequest;
import com.commerce.backend.model.response.user.UserResponse;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserResponseConverter userResponseConverter;

    private Faker faker;

    private String userName;


    @BeforeEach
    public void setUp() {
        faker = new Faker();
        userName = faker.name().username();
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return userName;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }


        });
    }


    @Test
    void it_should_register_a_user() {

        // given
        String email = faker.lorem().word();
        String password = faker.lorem().word();
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(email);
        registerUserRequest.setPassword(password);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        User userExpected = new User();

        given(userRepository.existsByEmail(email)).willReturn(false);
        given(passwordEncoder.encode(password)).willReturn(password);
        given(userRepository.save(any(User.class))).willReturn(userExpected);

        // when
        User user = userService.register(registerUserRequest);

        // then
        verify(userRepository).save(userArgumentCaptor.capture());
        then(user).isEqualToComparingFieldByField(userExpected);
        then(userArgumentCaptor.getValue().getEmail()).isEqualTo(email);
        then(userArgumentCaptor.getValue().getPassword()).isEqualTo(password);
        then(userArgumentCaptor.getValue().getEmailVerified()).isEqualTo(0);

    }


    @Test
    void it_should_not_register_a_user_when_it_exists() {

        // given
        String email = faker.lorem().word();
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(email);

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> userService.register(registerUserRequest))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("An account already exists with this email");

    }

    @Test
    void it_should_fetch_user() {

        // given
        User user = new User();

        UserResponse userResponseExpected = new UserResponse();

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));
        given(userResponseConverter.apply(user)).willReturn(userResponseExpected);

        // when
        UserResponse userResponseResult = userService.fetchUser();

        // then
        verify(userRepository).findByEmail(userName);
        then(userResponseResult).isEqualTo(userResponseExpected);

    }

    @Test
    void it_should_throw_exception_when_user_name_is_null_fetch_user() {

        // given
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }


        });

        // when, then
        assertThatThrownBy(() -> userService.fetchUser())
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Invalid access");

    }

    @Test
    void it_should_throw_exception_when_user_is_null_found_by_email_fetch_user() {

        // given
        given(userRepository.findByEmail(userName)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userService.fetchUser())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

    }

    @Test
    void it_should_get_user() {

        // given
        User user = new User();

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));

        // when
        User userResult = userService.getUser();

        // then
        verify(userRepository).findByEmail(userName);
        then(userResult).isEqualTo(user);

    }

    @Test
    void it_should_throw_exception_when_user_name_is_null_get_user() {

        // given
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }


        });

        // when, then
        assertThatThrownBy(() -> userService.getUser())
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Invalid access");

    }

    @Test
    void it_should_throw_exception_when_user_is_null_found_by_email_get_user() {

        // given
        given(userRepository.findByEmail(userName)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userService.getUser())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

    }

    @Test
    void it_should_save_user() {

        // given
        User user = new User();

        given(userRepository.save(user)).willReturn(user);

        // when
        User userResult = userService.saveUser(user);

        // then
        verify(userRepository).save(user);
        then(userResult).isEqualTo(user);

    }

    @Test
    void it_should_throw_exception_user_when_it_is_null() {

        // when, then
        assertThatThrownBy(() -> userService.saveUser(null))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Null user");

    }

    @Test
    void it_should_find_user_by_email() {

        // given
        String email = faker.lorem().word();
        User user = new User();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        User userResult = userService.findByEmail(email);

        // then
        verify(userRepository).findByEmail(email);
        then(userResult).isEqualTo(user);

    }

    @Test
    void it_should_find_null_user_by_email() {

        // given
        String email = faker.lorem().word();

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when
        User userResult = userService.findByEmail(email);

        // then
        verify(userRepository).findByEmail(email);
        then(userResult).isEqualTo(null);

    }

    @Test
    void it_should_throw_exception_when_find_by_email_is_null() {

        // when, then
        assertThatThrownBy(() -> userService.findByEmail(null))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Null email");

    }

    @Test
    void it_should_return_true_if_user_exists() {

        // given
        String email = faker.lorem().word();

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        boolean doesUserExist = userService.userExists(email);

        // then
        verify(userRepository).existsByEmail(email);
        then(doesUserExist).isEqualTo(true);

    }

    @Test
    void it_should_return_false_if_user_does_not_exist() {

        // given
        String email = faker.lorem().word();

        given(userRepository.existsByEmail(email)).willReturn(false);

        // when
        boolean doesUserExist = userService.userExists(email);

        // then
        verify(userRepository).existsByEmail(email);
        then(doesUserExist).isEqualTo(false);

    }

    @Test
    void it_should_update_user() {

        // given
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        User user = new User();
        UserResponse userResponseExpected = new UserResponse();

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);
        given(userResponseConverter.apply(user)).willReturn(userResponseExpected);

        // when
        UserResponse userResponseResult = userService.updateUser(updateUserRequest);

        // then
        verify(userRepository).findByEmail(userName);
        verify(userRepository).save(user);
        verify(userResponseConverter).apply(user);
        then(userResponseResult).isEqualTo(userResponseExpected);

    }

    @Test
    void it_should_update_user_address() {

        // given
        UpdateUserAddressRequest updateUserAddressRequest = new UpdateUserAddressRequest();

        User user = new User();
        UserResponse userResponseExpected = new UserResponse();

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);
        given(userResponseConverter.apply(user)).willReturn(userResponseExpected);

        // when
        UserResponse userResponseResult = userService.updateUserAddress(updateUserAddressRequest);

        // then
        verify(userRepository).findByEmail(userName);
        verify(userRepository).save(user);
        verify(userResponseConverter).apply(user);
        then(userResponseResult).isEqualTo(userResponseExpected);

    }

    @Test
    void it_should_reset_password() {

        // given
        String oldPassword = faker.howIMetYourMother().character();
        String newPassword = faker.random().hex();
        String activePassword = faker.random().hex();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setOldPassword(oldPassword);
        passwordResetRequest.setNewPassword(newPassword);

        User user = new User();
        user.setPassword(activePassword);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(oldPassword, activePassword)).willReturn(true);
        given(passwordEncoder.matches(newPassword, activePassword)).willReturn(false);
        given(passwordEncoder.encode(newPassword)).willReturn(newPassword);
        given(userRepository.save(user)).willReturn(user);

        // when
        userService.resetPassword(passwordResetRequest);

        // then
        verify(userRepository).save(userArgumentCaptor.capture());

        then(user).isEqualTo(userArgumentCaptor.getValue());

    }

    @Test
    void it_should_throw_exception_when_old_password_and_new_password_is_not_equal() {

        //given
        given(userRepository.findByEmail(userName)).willReturn(Optional.of(new User()));
        given(passwordEncoder.matches(any(), any())).willReturn(false);


        // when, then
        assertThatThrownBy(() -> userService.resetPassword(new PasswordResetRequest()))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Invalid password");

    }

    @Test
    void it_should_return_true_if_get_verification_status_is_one() {

        // given
        User user = new User();
        user.setEmailVerified(1);

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));

        //when
        Boolean verificationStatus = userService.getVerificationStatus();

        //then
        then(verificationStatus).isEqualTo(true);

    }

    @Test
    void it_should_return_false_if_get_verification_status_is_not_one() {

        // given
        User user = new User();
        user.setEmailVerified(faker.number().numberBetween(2, 100));

        given(userRepository.findByEmail(userName)).willReturn(Optional.of(user));

        //when
        Boolean verificationStatus = userService.getVerificationStatus();

        //then
        then(verificationStatus).isEqualTo(false);

    }

}