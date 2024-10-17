package com.school.school.service;

import com.school.school.dto.CustomUserDetails;
import com.school.school.dto.User;
import com.school.school.repository.UserRepository;
import com.school.school.utils.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws Exception {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new Exception("User with email " + email + " not found", HttpStatus.NOT_FOUND));
        return new CustomUserDetails(user);
    }
}

