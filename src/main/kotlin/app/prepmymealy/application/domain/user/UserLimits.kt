package app.prepmymealy.application.domain.user

data class UserLimits(val regenerateRequestsPerWeek: Int = 2) {

    companion object {
        fun builder() = Builder()
    }

    fun toBuilder() = Builder(regenerateRequestsPerWeek)

    data class Builder(
        var regenerateRequestsPerWeek: Int = 2
    ) {
        fun regenerateRequestsPerWeek(regenerateRequestsPerWeek: Int) = apply { this.regenerateRequestsPerWeek = regenerateRequestsPerWeek }
        fun build() = UserLimits(regenerateRequestsPerWeek)
    }
}
