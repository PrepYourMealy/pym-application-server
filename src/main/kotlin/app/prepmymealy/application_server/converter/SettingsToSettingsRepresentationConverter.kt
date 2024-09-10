package app.prepmymealy.application_server.converter

import app.prepmymealy.application_server.domain.Settings
import app.prepmymealy.application_server.representation.SettingsRepresentation
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
                it.people,
                it.mealsPerDay
            )
        }
    }
}