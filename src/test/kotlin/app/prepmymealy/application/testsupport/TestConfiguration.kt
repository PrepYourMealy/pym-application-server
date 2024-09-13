package app.prepmymealy.application.testsupport

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.time.Clock

@Configuration
class TestConfiguration {
    class CustomResponseErrorHandler : ResponseErrorHandler {
        override fun hasError(clientHttpResponse: ClientHttpResponse): Boolean {
            return false
        }

        override fun handleError(clientHttpResponse: ClientHttpResponse) {
        }
    }

    @Bean(name = ["defaultClock"])
    fun defaultClock(): Clock {
        return Clock.systemDefaultZone()
    }

    @Bean
    fun mongoClient(container: MongoDbTestContainer): MongoClient {
        return MongoClients.create(container.getConnectionString())
    }

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory()
        factory.setConnectTimeout(2000) // 2 seconds
        factory.setReadTimeout(2000)
        val template = RestTemplate(factory)
        template.errorHandler = CustomResponseErrorHandler()
        return template
    }
}
