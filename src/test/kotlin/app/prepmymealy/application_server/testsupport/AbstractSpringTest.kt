package app.prepmymealy.application_server.testsupport

import app.prepmymealy.application_server.Server
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [Server::class, TestConfiguration::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ["server.port=3000"])
@ActiveProfiles("test")
abstract class AbstractSpringTest {
}