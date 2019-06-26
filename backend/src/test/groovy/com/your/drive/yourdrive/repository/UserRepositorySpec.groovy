package com.your.drive.yourdrive.repository


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import java.time.Instant

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    UserRepository userRepository

    def "should inject proper time to DateAudit"() {
        given:
        Instant startTest = Instant.now()
        User givenUser = new User("fake@EMAIL.com", "hashedPassword")

        givenUser.createdAt = startTest
        givenUser.updatedAt = startTest

        expect:
        givenUser.id == null

        when:
        userRepository.save(givenUser)
        User receivedUser = userRepository.findByEmail(givenUser.email).get()

        then:
        receivedUser == givenUser
    }
}
