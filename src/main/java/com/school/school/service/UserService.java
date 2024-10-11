package com.school.school.service;

import com.school.school.dto.User;
import com.school.school.repository.UserRepository;
import com.school.school.utils.Exception;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        User user = userRepository.findById(id);

        if (Objects.isNull(user)) {
            throw new Exception("user not found" + id);

        } else return user;
    }

    public User createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.password());  // Encode the password
        User newUser = new User(null, user.firstname(), user.lastname(), user.email(), encodedPassword, user.role());
        return userRepository.save(newUser);
    }

    public User update(User user, Long id) {
        String encodedPassword = passwordEncoder.encode(user.password());  // Encode the password
        User newUser = new User(id, user.firstname(), user.lastname(), user.email(), encodedPassword, user.role());
        int result = userRepository.update(newUser, id);
        if (result == 0) {
            throw new Exception("user not found" + id);
        } else return findUserById(id);
    }

    public void encodeUsersPassword() {
        List<User> users = userRepository.findAll();
        users.stream().forEach(user -> {
            String encodedPassword = passwordEncoder.encode(user.password());  // Encode the password
            User newUser = new User(user.id(), user.firstname(), user.lastname(), user.email(), encodedPassword, user.role());
            userRepository.update(newUser, user.id());
        });
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
