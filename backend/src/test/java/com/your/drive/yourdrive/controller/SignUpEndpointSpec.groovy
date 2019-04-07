package com.your.drive.yourdrive.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.your.drive.yourdrive.Application
import com.your.drive.yourdrive.IntegrationTest
import com.your.drive.yourdrive.repository.User
import com.your.drive.yourdrive.repository.UserRepository
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@Category(IntegrationTest.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application]
)
class SignUpEndpointSpec extends Specification {
    @Autowired
    TestRestTemplate client

    @Autowired
    UserRepository userRepository

    @Autowired
    ObjectMapper objectMapper

    @LocalServerPort
    int port

    private final static String EMAIL = "fake@mail.com"
    private final static String JSON_REQUEST = "" +
            "{\n" +
            "\t\"email\": \"" + EMAIL + "\",\n" +
            "\t\"password\": \"dummyPassword\",\n" +
            "}"

    def "should deserialize SignUpRequest"() {
        given:
        SignUpRequest expectedRequest = SignUpRequest.builder()
                .email(EMAIL)
                .password("dummyPassword")
                .build()

        when:
        SignUpRequest actualRequest = objectMapper.readValue(JSON_REQUEST, SignUpRequest.class)

        then:
        expectedRequest == actualRequest
    }

    def "should create a new user"() {
        when :
        ResponseEntity<RegistrationResponse> responseEntity = client.exchange(createRequest(), RegistrationResponse.class)

        then:
        responseEntity.statusCode == HttpStatus.CREATED

        and:
        with(responseEntity.body) {
            success
            message == "User registered successfully"
        }

        cleanup:
        if (userRepository.existsByEmail(EMAIL)) {
            userRepository.deleteByEmail(EMAIL)
        }
    }

    def "should return message that the email is already in use"() {
        given:
        User existingUser = new User(EMAIL, "dummyPassword123")

        when:
        userRepository.save(existingUser)
        ResponseEntity<RegistrationResponse> responseEntity = client.exchange(createRequest(), RegistrationResponse.class)

        then:
        responseEntity.statusCode == HttpStatus.BAD_REQUEST

        and:
        with(responseEntity.body) {
            !success
            message == "Email Address already in use!"
        }

        cleanup:
        if (userRepository.existsByEmail(EMAIL)) {
            userRepository.deleteByEmail(EMAIL)
        }
    }

    private RequestEntity<String> createRequest() {
        return RequestEntity
                .post(uri("/api/controller/signup"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(JSON_REQUEST)
    }

    private URI uri(String relativeUri) {
        "http://localhost:$port$relativeUri".toURI()
    }
}
