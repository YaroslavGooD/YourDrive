package com.your.drive.yourdrive.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RegistrationResponse {
    private Boolean success;
    private String message;
}
