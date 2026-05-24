package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Email
    String email,

    @Size(min = 5)
    String password
) {}
