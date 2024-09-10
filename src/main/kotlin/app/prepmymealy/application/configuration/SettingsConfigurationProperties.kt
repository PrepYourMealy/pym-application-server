package app.prepmymealy.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "settings.validation")
data class SettingsConfigurationProperties(
    val maxStringSize: Int,
    val maxFavoriteMealsLength: Int,
    val maxLikedIngredientsLength: Int,
    val maxDislikedIngredientsLength: Int,
    val maxAllergiesLength: Int,
    val maxDietaryPreferencesLength: Int,
    val maxKitchenEquipmentLength: Int,
    val minPeople: Int,
    val maxPeople: Int,
    val minBudget: Int,
    val maxBudget: Int,
    val minMealsPerDay: Int,
    val maxMealsPerDay: Int,
)
