package app.prepmymealy.application.domain.settings

data class ReducedWeeklySettings(
    val overall: DailySettings? = null,
) {
    companion object {
        fun builder() = Builder()
    }

    fun toBuilder() = Builder(overall)

    data class Builder(
        var overall: DailySettings? = null,
    ) {
        fun overall(overall: DailySettings) = apply { this.overall = overall }
        fun build() = ReducedWeeklySettings(overall)
    }
}
