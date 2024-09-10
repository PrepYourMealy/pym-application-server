package app.prepmymealy.application.testsupport

import app.prepmymealy.application.Server
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [Server::class, TestConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = ["server.port=3000"],
)
@ActiveProfiles("test")
abstract class AbstractSpringTest
