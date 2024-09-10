package app.prepmymealy.application.service

import app.prepmymealy.application.configuration.SettingsConfigurationProperties
import app.prepmymealy.application.domain.Settings
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class SettingsValidationService(
    private val settingLimits: SettingsConfigurationProperties,
) {
    companion object {
        private const val MAX_STRING_SIZE_EXCEEDED = "The given string exceeds the limit."
        private const val MAX_FAVORITE_MEALS_EXCEEDED = "Favorite meals array exceeds the limit."
        private const val MAX_LIKED_INGREDIENTS_EXCEEDED = "Liked ingredients exceeds the limit."
        private const val MAX_DISLIKED_INGREDIENTS_EXCEEDED = "Disliked ingredients exceeds the limit."
        private const val MAX_ALLERGIES_EXCEEDED = "Allergies exceeds the limit."
        private const val MAX_DIETARY_PREFERENCES_EXCEEDED = "Dietary exceeds the limit."
        private const val MAX_KITCHEN_EQUIPMENT_EXCEEDED = "Kitchen exceeds the limit."
    }

    fun validateSettings(settings: Settings): Optional<Map<String, String>> {
        val errorMap = mutableMapOf<String, String>()

        // Validate numeric properties with limits
        validateNumeric(settings.people, settingLimits.minPeople, settingLimits.maxPeople, "people", errorMap)
        validateNumeric(settings.budget, settingLimits.minBudget, settingLimits.maxBudget, "budget", errorMap)
        validateNumeric(settings.mealsPerDay, settingLimits.minMealsPerDay, settingLimits.maxMealsPerDay, "mealsPerDay", errorMap)

        // Validate collection lengths
        validateCollectionSize(
            settings.favoriteMeals,
            settingLimits.maxFavoriteMealsLength,
            "favoriteMeals",
            MAX_FAVORITE_MEALS_EXCEEDED,
            errorMap,
        )
        validateCollectionSize(
            settings.likedIngredients,
            settingLimits.maxLikedIngredientsLength,
            "likedIngredients",
            MAX_LIKED_INGREDIENTS_EXCEEDED,
            errorMap,
        )
        validateCollectionSize(
            settings.dislikedIngredients,
            settingLimits.maxDislikedIngredientsLength,
            "dislikedIngredients",
            MAX_DISLIKED_INGREDIENTS_EXCEEDED,
            errorMap,
        )
        validateCollectionSize(settings.allergies, settingLimits.maxAllergiesLength, "allergies", MAX_ALLERGIES_EXCEEDED, errorMap)
        validateCollectionSize(
            settings.dietaryPreferences,
            settingLimits.maxDietaryPreferencesLength,
            "dietaryPreferences",
            MAX_DIETARY_PREFERENCES_EXCEEDED,
            errorMap,
        )
        validateCollectionSize(
            settings.kitchenEquipment,
            settingLimits.maxLikedIngredientsLength,
            "kitchenEquipment",
            MAX_KITCHEN_EQUIPMENT_EXCEEDED,
            errorMap,
        )

        // Validate string lengths within collections
        validateStringLength(settings.favoriteMeals, "favoriteMeals", errorMap)
        validateStringLength(settings.likedIngredients, "likedIngredients", errorMap)
        validateStringLength(settings.dislikedIngredients, "dislikedIngredients", errorMap)
        validateStringLength(settings.allergies, "allergies", errorMap)
        validateStringLength(settings.dietaryPreferences, "dietaryPreferences", errorMap)
        validateStringLength(settings.kitchenEquipment, "kitchenEquipment", errorMap)

        return if (errorMap.isEmpty()) Optional.empty() else Optional.of(errorMap)
    }

    private fun validateNumeric(
        value: Long?,
        min: Int,
        max: Int,
        field: String,
        errorMap: MutableMap<String, String>,
    ) {
        value?.let {
            when {
                it < min -> errorMap[field] = "$field is below the minimum."
                it > max -> errorMap[field] = "$field has exceeded the maximum."
            }
        }
    }

    private fun validateCollectionSize(
        collection: List<*>?,
        maxSize: Int,
        field: String,
        errorMessage: String,
        errorMap: MutableMap<String, String>,
    ) {
        if ((collection?.size ?: 0) > maxSize) {
            errorMap[field] = errorMessage
        }
    }

    private fun validateStringLength(
        strings: List<String>?,
        field: String,
        errorMap: MutableMap<String, String>,
    ) {
        strings?.forEachIndexed { index, value ->
            if (value.length > settingLimits.maxStringSize) {
                errorMap["$field[$index]"] = MAX_STRING_SIZE_EXCEEDED
            }
        }
    }
}
