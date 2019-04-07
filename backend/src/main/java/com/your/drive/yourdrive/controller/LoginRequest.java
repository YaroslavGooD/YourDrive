package com.your.drive.yourdrive.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
