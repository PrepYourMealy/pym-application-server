package app.prepmymealy.application.converter

import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.representation.SettingsRepresentation
import org.springframework.stereotype.Component

@Component
class SettingsToSettingsRepresentationConverter : Converter<Settings?, SettingsRepresentation?> {
    override fun convert(input: Settings?): SettingsRepresentation? {
        return input?.let {
            SettingsRepresentation(
                it.id,
                it.budget,
                it.favoriteMeals,
                it.likedIngredients,
                it.dislikedIngredients,
                it.allergies,
                it.dietaryPreferences,
                it.kitchenEquipment,
                it.includeDiscounts,
            )
        }
    }
}
