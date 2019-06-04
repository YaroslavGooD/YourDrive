package com.your.drive.yourdrive


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest(
        classes = Application,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ApplicationSpec extends Specification {

    @Autowired
    ApplicationContext context

    def "context loads"() {
        expect:
        context != null
    }
}
