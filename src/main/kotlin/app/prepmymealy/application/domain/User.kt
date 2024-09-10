package app.prepmymealy.application.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String,
    val stats: UserStats? = null,
    val limits: UserLimits? = null,
)
