package app.prepmymealy.application_server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "settings")
data class Settings(
    @Id
    val id: String,
    val budget: Long,
    val favoriteMeals: List<String>,
    val likedIngredients: List<String>,
    val dislikedIngredients: List<String>,
    val allergies: List<String>,
    val dietaryPreferences: List<String>,
    val kitchenEquipment: List<String>,
    val includeDiscounts: Boolean,
    val people: Long,
    val mealsPerDay: Long
)
