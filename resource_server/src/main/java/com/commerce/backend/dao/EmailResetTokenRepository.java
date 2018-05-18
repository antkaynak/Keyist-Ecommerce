package com.commerce.backend.dao;

import com.commerce.backend.model.EmailResetToken;
import com.commerce.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailResetTokenRepository extends CrudRepository<EmailResetToken, Long> {
    EmailResetToken findByToken(String token);

    EmailResetToken findByUser(User user);
}
