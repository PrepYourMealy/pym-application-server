package app.prepmymealy.application.domain.settings

data class DailySettings(
    val breakfast: MenuSettings? = null,
    val lunch: MenuSettings? = null,
    val dinner: MenuSettings? = null,
) {
    companion object {
        fun builder() = Builder()
    }

    fun toPrompt(): String {
        val breakfastPrompt = if (breakfast != null) "Das Frühstück ${breakfast.toPrompt()}" else ""
        val lunchPrompt = if (lunch != null) "Das Mittagessen ${lunch.toPrompt()}" else ""
        val dinnerPrompt = if (dinner != null) "Das Abendessen ${dinner.toPrompt()}" else ""
        return "$breakfastPrompt $lunchPrompt $dinnerPrompt"
    }

    fun toBuilder() = Builder(breakfast, lunch, dinner)

    data class Builder(
        var breakfast: MenuSettings? = null,
        var lunch: MenuSettings? = null,
        var dinner: MenuSettings? = null,
    ) {
        fun breakfast(breakfast: MenuSettings) = apply { this.breakfast = breakfast }

        fun lunch(lunch: MenuSettings) = apply { this.lunch = lunch }

        fun dinner(dinner: MenuSettings) = apply { this.dinner = dinner }

        fun build() = DailySettings(breakfast, lunch, dinner)
    }
}
