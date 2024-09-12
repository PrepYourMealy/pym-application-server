package app.prepmymealy.application.domain.settings

data class WeeklySettings(
    var monday: DailySettings? = null,
    var tuesday: DailySettings? = null,
    var wednesday: DailySettings? = null,
    var thursday: DailySettings? = null,
    var friday: DailySettings? = null,
    var saturday: DailySettings? = null,
    var sunday: DailySettings? = null,
)
