package app.prepmymealy.application.domain.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String,
    var stats: UserStats? = null,
    var limits: UserLimits? = null,


    ) {

    companion object {
        fun builder(id: String) = Builder(id)
    }

    fun toBuilder() = Builder(id, stats, limits)

    data class Builder(
        var id: String,
        var stats: UserStats? = null,
        var limits: UserLimits? = null
    ) {
        fun stats(stats: UserStats) = apply { this.stats = stats }
        fun limits(limits: UserLimits) = apply { this.limits = limits }
        fun build() = User(id, stats, limits)
    }

}
