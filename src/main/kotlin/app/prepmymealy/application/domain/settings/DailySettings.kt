package app.prepmymealy.application.domain.settings

data class DailySettings(
    val overall: MenuSettings? = null,
    val breakfast: MenuSettings? = null,
    val lunch: MenuSettings? = null,
    val dinner: MenuSettings? = null,
)
