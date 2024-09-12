package app.prepmymealy.application.domain.settings

data class DailySettings(
    val overall: MenuSettings? = null,
    val breakfast: MenuSettings? = null,
    val lunch: MenuSettings? = null,
    val dinner: MenuSettings? = null,
) {
    companion object {
        fun builder() = Builder()
    }

    fun toBuilder() = Builder(overall, breakfast, lunch, dinner)

    data class Builder(
        var overall: MenuSettings? = null,
        var breakfast: MenuSettings? = null,
        var lunch: MenuSettings? = null,
        var dinner: MenuSettings? = null,
    ) {
        fun overall(overall: MenuSettings) = apply { this.overall = overall }
        fun breakfast(breakfast: MenuSettings) = apply { this.breakfast = breakfast }
        fun lunch(lunch: MenuSettings) = apply { this.lunch = lunch }
        fun dinner(dinner: MenuSettings) = apply { this.dinner = dinner }
        fun build() = DailySettings(overall, breakfast, lunch, dinner)
    }
}
