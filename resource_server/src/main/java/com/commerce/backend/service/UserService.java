package com.commerce.backend.service;


import com.commerce.backend.dto.PasswordResetDTO;
import com.commerce.backend.dto.UserDTO;
import com.commerce.backend.model.User;

import java.security.Principal;

public interface UserService {

    /* Public */
    User register(UserDTO userDTO);

    /* Secured */
    User getUser(Principal principal);

    User updateUser(Principal principal, User user);

    void resetPassword(Principal principal, PasswordResetDTO passwordResetDTO);

    Boolean getVerificationStatus(Principal principal);
}
