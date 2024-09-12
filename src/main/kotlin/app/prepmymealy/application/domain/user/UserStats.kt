package app.prepmymealy.application.domain.user

data class UserStats(val weeklyRegenerateRequest: Int = 0) {
    companion object {
        fun builder() = Builder()
    }

    fun toBuilder() = Builder(weeklyRegenerateRequest)

    data class Builder(
        var weeklyRegenerateRequest: Int = 0
    ) {
        fun weeklyRegenerateRequest(weeklyRegenerateRequest: Int) = apply { this.weeklyRegenerateRequest = weeklyRegenerateRequest }
        fun build() = UserStats(weeklyRegenerateRequest)
    }
}
