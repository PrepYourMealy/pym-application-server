package app.prepmymealy.application.testsupport

import app.prepmymealy.application.Server
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.nio.charset.StandardCharsets

@SpringBootTest(
    classes = [Server::class, TestConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = ["server.port=3000"],
)
@DirtiesContext
@ActiveProfiles("test")
@ExtendWith(MockitoExtension::class)
abstract class AbstractSpringTest {
    fun loadResourceAsString(resourcePath: String): String {
        return object {}.javaClass.getResourceAsStream(resourcePath)?.bufferedReader(StandardCharsets.UTF_8)?.use { it.readText() }
            ?: throw IllegalArgumentException("Resource not found: $resourcePath")
    }
}
