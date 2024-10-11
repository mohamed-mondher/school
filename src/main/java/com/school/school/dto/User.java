package com.school.school.dto;

public record User(Long id, String firstname,String lastname, String email, String password, Role role) {}

