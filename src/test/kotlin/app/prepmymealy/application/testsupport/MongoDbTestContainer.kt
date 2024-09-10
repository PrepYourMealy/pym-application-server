package app.prepmymealy.application.testsupport

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import java.util.*

@Component
class MongoDbTestContainer {
    companion object {
        private val MONGODB_VERSION: String = Optional.ofNullable(System.getenv("MONGODB_VERSION")).orElse("latest")
        private val MONGODB_IMAGE = DockerImageName.parse("mongo:$MONGODB_VERSION")
    }

    val container: MongoDBContainer = MongoDBContainer(MONGODB_IMAGE)

    @PostConstruct
    fun start() {
        container.start()
    }

    @PreDestroy
    fun stop() {
        container.stop()
    }

    fun getConnectionString(): String {
        return container.connectionString
    }
}
