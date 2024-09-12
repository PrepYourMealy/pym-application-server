package app.prepmymealy.application.testsupport

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.boot.test.mock.mockito.MockBean

@Configuration
class TestConfiguration {

    @Bean(name = ["defaultClock"])
    fun defaultClock(): Clock {
        return Clock.systemDefaultZone()
    }

    @Bean
    fun mongoClient(container: MongoDbTestContainer): MongoClient {
        return MongoClients.create(container.getConnectionString())
    }

    @MockBean(name = "openAiChatModel")
    private lateinit var openAiChatModel: OpenAiChatModel
}
