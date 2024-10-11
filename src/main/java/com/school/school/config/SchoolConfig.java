package com.school.school.config;

import com.school.school.repository.UserRepository;
import com.school.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SchoolConfig {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public SchoolConfig(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepository();
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository(), passwordEncoder);
    }
}
