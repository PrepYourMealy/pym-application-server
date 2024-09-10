package app.prepmymealy.application.service

import app.prepmymealy.application.domain.Settings
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class SettingsValidationService(
    @Value("\${settings.validation.max-string-size}") private val maxStringSize: Int,
    @Value("\${settings.validation.max-favorite-meals-length}") private val maxFavoriteMeals: Int,
    @Value("\${settings.validation.max-liked-ingredients-length}") private val maxLikedIngredients: Int,
    @Value("\${settings.validation.max-disliked-ingredients-length}") private val maxDislikedIngredients: Int,
    @Value("\${settings.validation.max-allergies-length}") private val maxAllergies: Int,
    @Value("\${settings.validation.max-dietary-preferences}") private val maxDietaryPreferences: Int,
    @Value("\${settings.validation.max-kitchen-equipment}") private val maxKitchenEquipment: Int,
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
        if (settings.favoriteMeals?.size!! > maxFavoriteMeals) {
            errorMap["favoriteMeals"] = MAX_FAVORITE_MEALS_EXCEEDED
        }
        if (settings.likedIngredients?.size!! > maxLikedIngredients) {
            errorMap["likedIngredients"] = MAX_LIKED_INGREDIENTS_EXCEEDED
        }
        if (settings.dislikedIngredients?.size!! > maxDislikedIngredients) {
            errorMap["dislikedIngredients"] = MAX_DISLIKED_INGREDIENTS_EXCEEDED
        }
        if (settings.allergies?.size!! > maxAllergies) {
            errorMap["allergies"] = MAX_ALLERGIES_EXCEEDED
        }
        if (settings.dietaryPreferences?.size!! > maxDietaryPreferences) {
            errorMap["dietaryPreferences"] = MAX_DIETARY_PREFERENCES_EXCEEDED
        }
        if (settings.kitchenEquipment?.size!! > maxKitchenEquipment) {
            errorMap["kitchenEquipment"] = MAX_KITCHEN_EQUIPMENT_EXCEEDED
        }
        validateStringLength(settings.favoriteMeals, "favoriteMeals", errorMap)
        validateStringLength(settings.likedIngredients, "likedIngredients", errorMap)
        validateStringLength(settings.dislikedIngredients, "dislikedIngredients", errorMap)
        validateStringLength(settings.allergies, "allergies", errorMap)
        validateStringLength(settings.dietaryPreferences, "dietaryPreferences", errorMap)
        validateStringLength(settings.kitchenEquipment, "kitchenEquipment", errorMap)
        if (errorMap.isEmpty()) {
            return Optional.empty()
        }
        return Optional.of(errorMap)
    }

    private fun validateStringLength(
        strings: List<String>?,
        name: String,
        errorMap: MutableMap<String, String>,
    ) {
        if (strings.isNullOrEmpty()) {
            return
        }
        for (i in strings.indices) {
            if (strings[i].length > maxStringSize) {
                errorMap["$name[$i]"] = MAX_STRING_SIZE_EXCEEDED
            }
        }
    }
}
