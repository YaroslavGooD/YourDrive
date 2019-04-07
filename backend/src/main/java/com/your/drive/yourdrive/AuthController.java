package com.your.drive.yourdrive;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
class AuthController {

    @RequestMapping("/user")
    Principal user(Principal principal) {
        return principal;
    }
}
