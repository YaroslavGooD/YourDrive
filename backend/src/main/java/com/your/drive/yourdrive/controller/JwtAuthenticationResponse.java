package com.your.drive.yourdrive.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
