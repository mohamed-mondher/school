package com.school.school.controller;


import com.school.school.dto.AuthRequest;
import com.school.school.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/v1/login")
    public String login(@RequestBody AuthRequest request) {
        return authService.auth(request.email(), request.password());
    }

}
