package com.your.drive.yourdrive.controller

import com.your.drive.yourdrive.Application
import com.your.drive.yourdrive.repository.User
import com.your.drive.yourdrive.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application]
)
class LoginEndpointSpec extends Specification {

    @Autowired
    TestRestTemplate client

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder

    @LocalServerPort
    int port

    private static final String EMAIL = "fake@mail.com"
    private static final String PASSWORD = "dummyPassword"

    def "should authenticate user successfully"() {
        given:
        User user = new User(EMAIL, passwordEncoder.encode(PASSWORD))
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD)
        RequestEntity<LoginRequest> requestEntity = RequestEntity
                .post(uri("/api/auth/login"))
                .body(loginRequest)

        when:
        if (userRepository.existsByEmail(EMAIL)) {
            userRepository.deleteByEmail(EMAIL)
        }
        userRepository.save(user)
        ResponseEntity<JwtAuthenticationResponse> response = client.exchange(requestEntity, JwtAuthenticationResponse.class)

        then:
        response.statusCode == HttpStatus.OK
        with(response.body) {
            accessToken.length() > 0
            tokenType.length() > 0
        }

        cleanup:
        if (userRepository.existsByEmail(EMAIL)) {
            userRepository.deleteByEmail(EMAIL)
        }
    }

    private URI uri(String relativeUri) {
        "http://localhost:$port$relativeUri".toURI()
    }
}