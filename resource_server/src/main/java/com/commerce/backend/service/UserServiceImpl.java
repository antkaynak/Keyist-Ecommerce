package com.commerce.backend.service;

import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.dto.PasswordResetDTO;
import com.commerce.backend.dto.UserDTO;
import com.commerce.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalStateException("An account already exists with this email");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmailVerified(0);
        userRepository.save(user);
        return user;
    }


    @Override
    public User getUser(Principal principal) {
        return getUserFromPrinciple(principal);
    }

    @Override
    public User updateUser(Principal principal, User user) {
        User dbUser = getUserFromPrinciple(principal);
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setAddress(user.getAddress());
        dbUser.setAddress2(user.getAddress2());
        dbUser.setCity(user.getCity());
        dbUser.setState(user.getState());
        dbUser.setZip(user.getZip());
        dbUser.setCountry(user.getCountry());
        dbUser.setPhone(user.getPhone());

        System.out.println(dbUser);
        userRepository.save(dbUser);
        return dbUser;
    }

    @Override
    public void resetPassword(Principal principal, PasswordResetDTO passwordResetDTO) {
        User user = getUserFromPrinciple(principal);
        if (!passwordEncoder.matches(passwordResetDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        if (passwordEncoder.matches(passwordResetDTO.getNewPassword(), user.getPassword())) {
            //throw new IllegalCallerException("Same password"); do not reveal user's password
            //just skip setting the new password since it is the same as the old one.
            return;
        }

        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Boolean getVerificationStatus(Principal principal) {
        User user = getUserFromPrinciple(principal);
        return user.getEmailVerified() == 1;
    }

    private User getUserFromPrinciple(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("Invalid access");
        }
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }
}
