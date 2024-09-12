package app.prepmymealy.application.domain.settings

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "settings")
data class Settings
    @JsonCreator
    constructor(
        @Id
        val id: String,
        val budget: Long? = null,
        val favoriteMeals: List<String>? = null,
        val likedIngredients: List<String>? = null,
        val dislikedIngredients: List<String>? = null,
        val allergies: List<String>? = null,
        val dietaryPreferences: List<String>? = null,
        val kitchenEquipment: List<String>? = null,
        val includeDiscounts: Boolean? = null,
        val overallWeeklySettings: ReducedWeeklySettings? = null,
        val weeklySettings: WeeklySettings? = null,
    ) {
        companion object {
            fun builder(id: String) = Builder(id)
        }

        fun toBuilder() =
            Builder(
                id,
                budget,
                favoriteMeals,
                likedIngredients,
                dislikedIngredients,
                allergies,
                dietaryPreferences,
                kitchenEquipment,
                includeDiscounts,
                overallWeeklySettings,
                weeklySettings,
            )

        data class Builder(
            var id: String,
            var budget: Long? = null,
            var favoriteMeals: List<String>? = null,
            var likedIngredients: List<String>? = null,
            var dislikedIngredients: List<String>? = null,
            var allergies: List<String>? = null,
            var dietaryPreferences: List<String>? = null,
            var kitchenEquipment: List<String>? = null,
            var includeDiscounts: Boolean? = null,
            var overallWeeklySettings: ReducedWeeklySettings? = null,
            var weeklySettings: WeeklySettings? = null,
        ) {
            fun budget(budget: Long) = apply { this.budget = budget }

            fun favoriteMeals(favoriteMeals: List<String>) = apply { this.favoriteMeals = favoriteMeals }

            fun likedIngredients(likedIngredients: List<String>) = apply { this.likedIngredients = likedIngredients }

            fun dislikedIngredients(dislikedIngredients: List<String>) = apply { this.dislikedIngredients = dislikedIngredients }

            fun allergies(allergies: List<String>) = apply { this.allergies = allergies }

            fun dietaryPreferences(dietaryPreferences: List<String>) = apply { this.dietaryPreferences = dietaryPreferences }

            fun kitchenEquipment(kitchenEquipment: List<String>) = apply { this.kitchenEquipment = kitchenEquipment }

            fun includeDiscounts(includeDiscounts: Boolean) = apply { this.includeDiscounts = includeDiscounts }

            fun overallWeeklySettings(overallWeeklySettings: ReducedWeeklySettings) =
                apply { this.overallWeeklySettings = overallWeeklySettings }

            fun weeklySettings(weeklySettings: WeeklySettings) = apply { this.weeklySettings = weeklySettings }

            fun build() =
                Settings(
                    id,
                    budget,
                    favoriteMeals,
                    likedIngredients,
                    dislikedIngredients,
                    allergies,
                    dietaryPreferences,
                    kitchenEquipment,
                    includeDiscounts,
                    overallWeeklySettings,
                    weeklySettings,
                )
        }
    }
