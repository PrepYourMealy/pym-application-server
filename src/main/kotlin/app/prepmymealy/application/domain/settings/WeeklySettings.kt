package app.prepmymealy.application.domain.settings

data class WeeklySettings(
    val monday: DailySettings? = null,
    val tuesday: DailySettings? = null,
    val wednesday: DailySettings? = null,
    val thursday: DailySettings? = null,
    val friday: DailySettings? = null,
    val saturday: DailySettings? = null,
    val sunday: DailySettings? = null,
)
