package app.prepmymealy.application.domain.user

import app.prepmymealy.application.domain.menu.Menu
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String,
    var stats: UserStats? = null,
    var limits: UserLimits? = null,
    var isCreatingMenu: Boolean = false,
) {
    companion object {
        fun builder(id: String) = Builder(id)
    }

    fun toBuilder() = Builder(id, stats, limits, isCreatingMenu)

    data class Builder(
        var id: String,
        var stats: UserStats? = null,
        var limits: UserLimits? = null,
        var isCreatingMenu: Boolean = false,
    ) {
        fun stats(stats: UserStats) = apply { this.stats = stats }

        fun limits(limits: UserLimits) = apply { this.limits = limits }

        fun isCreatingMenu(isCreatingMenu: Boolean) = apply { this.isCreatingMenu = isCreatingMenu }

        fun build() = User(id, stats, limits, isCreatingMenu)
    }
}
