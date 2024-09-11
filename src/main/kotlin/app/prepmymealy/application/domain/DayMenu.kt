package app.prepmymealy.application.domain

data class DayMenu(
    val breakfast: Recipe? = null,
    val lunch: Recipe? = null,
    val dinner: Recipe? = null,
)
