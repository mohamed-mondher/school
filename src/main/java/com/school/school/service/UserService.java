package com.school.school.service;

import com.school.school.dto.User;
import com.school.school.repository.UserRepository;
import com.school.school.utils.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)).orElseThrow(() -> new Exception("User with id " + id + " not found", HttpStatus.NOT_FOUND));
    }

    public User createUser(User user) {
        User byEmail = userRepository.findByEmail(user.email());
        if (Objects.nonNull(byEmail)) {
            throw new Exception("User with email " + user.email() + " already exist", HttpStatus.CONFLICT);
        }

        String encodedPassword = passwordEncoder.encode(user.password());  // Encode the password
        User newUser = new User(null, user.firstname(), user.lastname(), user.email(), encodedPassword, user.role());
        return userRepository.save(newUser);
    }

    public User update(User user, Long id) {
        findUserById(id);
        String encodedPassword = passwordEncoder.encode(user.password());  // Encode the password
        User newUser = new User(id, user.firstname(), user.lastname(), user.email(), encodedPassword, user.role());
        userRepository.update(newUser, id);
        return findUserById(id);
    }

    public void deleteById(Long id) {
        findUserById(id);
        userRepository.deleteById(id);
    }
}
