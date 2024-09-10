package app.prepmymealy.application_server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "settings")
data class Settings(
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
    val people: Long? = null,
    val mealsPerDay: Long? = null
)
