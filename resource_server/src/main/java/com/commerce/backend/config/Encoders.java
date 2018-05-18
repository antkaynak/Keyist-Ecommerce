package com.commerce.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Encoders {

    @Bean
    public PasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}