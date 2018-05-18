package com.commerce.backend.dao;

import com.commerce.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    Boolean existsByEmail(String email);
}
