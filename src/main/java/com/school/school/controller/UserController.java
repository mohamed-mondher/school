package com.school.school.controller;

import com.school.school.dto.User;
import com.school.school.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/v1/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/v1/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @PostMapping("/v1/users/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping("/v1/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.update(user, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PutMapping("/v1/users")
    public String encodeUsersPassword() {
        userService.encodeUsersPassword();
        return "User updated successfully";
    }

}