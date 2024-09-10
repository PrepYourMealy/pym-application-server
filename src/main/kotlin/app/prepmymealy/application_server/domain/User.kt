package app.prepmymealy.application_server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String,
    val stats: UserStats,
    val limits: UserLimits
)
