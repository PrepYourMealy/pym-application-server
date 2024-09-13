package app.prepmymealy.application.testsupport

import app.prepmymealy.application.Server
import app.prepmymealy.application.acceptance.api.ControllerApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeClass
import java.nio.charset.StandardCharsets

@SpringBootTest(
    classes = [Server::class, TestConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ActiveProfiles("test")
abstract class AbstractSpringTest : AbstractTestNGSpringContextTests() {
    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    protected lateinit var api: ControllerApi

    @BeforeClass
    fun setUpClass() {
        api.setPort(port)
    }

    fun loadResourceAsString(resourcePath: String): String {
        return object {}.javaClass.getResourceAsStream(resourcePath)?.bufferedReader(StandardCharsets.UTF_8)?.use { it.readText() }
            ?: throw IllegalArgumentException("Resource not found: $resourcePath")
    }
}
