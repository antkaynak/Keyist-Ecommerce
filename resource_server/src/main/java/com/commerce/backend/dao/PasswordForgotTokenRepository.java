package com.commerce.backend.dao;

import com.commerce.backend.model.PasswordForgotToken;
import com.commerce.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordForgotTokenRepository extends CrudRepository<PasswordForgotToken, Long> {
    PasswordForgotToken findByToken(String token);

    PasswordForgotToken findByUser(User user);
}
