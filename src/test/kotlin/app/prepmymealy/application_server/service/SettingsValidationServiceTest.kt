package app.prepmymealy.application_server.service

import app.prepmymealy.application_server.domain.Settings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SettingsValidationServiceTest {

    private val settingsValidationService = SettingsValidationService(
        maxStringSize = 10,
        maxDietaryPreferences = 3,
        maxAllergies = 3,
        maxFavoriteMeals = 3,
        maxKitchenEquipment = 3,
        maxLikedIngredients = 3,
        maxDislikedIngredients = 3
    )

    @Test
    fun `should invalidate too long string`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1", "Meal2"),
            likedIngredients = listOf("Ingredient1"),
            dislikedIngredients = listOf("Ingredient1"),
            allergies = listOf("Allergy1"),
            dietaryPreferences = listOf("Dietary Preference1"),
            kitchenEquipment = listOf("KitchenEquipment1"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(4)
        assertThat(result.get()).containsEntry("likedIngredients[0]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("dislikedIngredients[0]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("dietaryPreferences[0]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("kitchenEquipment[0]", "The given string exceeds the limit.")
    }

    @Test
    fun `should invalidate when favorite meals array too long`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1", "Meal2", "Meal3", "Meal4"),
            likedIngredients = listOf("Ingre"),
            dislikedIngredients = listOf("Ingre"),
            allergies = listOf("Allergy1"),
            dietaryPreferences = listOf("Dietary"),
            kitchenEquipment = listOf("Kitchen"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(1)
        assertThat(result.get()).containsEntry("favoriteMeals", "Favorite meals array exceeds the limit.")
    }

    @Test
    fun `should invalidate when liked ingredients array too long`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1"),
            likedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3", "Ingredient4"),
            dislikedIngredients = listOf("Ingre"),
            allergies = listOf("Allergy1"),
            dietaryPreferences = listOf("Dietary"),
            kitchenEquipment = listOf("Kitchen"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(5)
        assertThat(result.get()).containsEntry("likedIngredients", "Liked ingredients exceeds the limit.")
        assertThat(result.get()).containsEntry("likedIngredients[0]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("likedIngredients[1]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("likedIngredients[2]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("likedIngredients[3]", "The given string exceeds the limit.")
    }

    @Test
    fun `should invalidate when disliked ingredients array too long`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1"),
            likedIngredients = listOf("Ingre"),
            dislikedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3", "Ingredient4"),
            allergies = listOf("Allergy1"),
            dietaryPreferences = listOf("Dietary"),
            kitchenEquipment = listOf("Kitchen"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(5)
        assertThat(result.get()).containsEntry("dislikedIngredients", "Disliked ingredients exceeds the limit.")
        assertThat(result.get()).containsEntry("dislikedIngredients[0]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("dislikedIngredients[1]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("dislikedIngredients[2]", "The given string exceeds the limit.")
        assertThat(result.get()).containsEntry("dislikedIngredients[3]", "The given string exceeds the limit.")
    }

    @Test
    fun `should invalidate when allergies array too long`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1"),
            likedIngredients = listOf("Ingre"),
            dislikedIngredients = listOf("Ingre"),
            allergies = listOf("Allergy1", "Allergy2", "Allergy3", "Allergy4"),
            dietaryPreferences = listOf("Dietary"),
            kitchenEquipment = listOf("Kitchen"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(1)
        assertThat(result.get()).containsEntry("allergies", "Allergies exceeds the limit.")
    }

    @Test
    fun `should invalidate when dietaryPreferences array too long`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1"),
            likedIngredients = listOf("Ingre"),
            dislikedIngredients = listOf("Ingre"),
            allergies = listOf("Allergy1"),
            dietaryPreferences = listOf("Dietary", "Dietary2", "Dietary3", "Dietary4"),
            kitchenEquipment = listOf("Kitchen"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(1)
        assertThat(result.get()).containsEntry("dietaryPreferences", "Dietary exceeds the limit.")
    }

    @Test
    fun `should invalidate when kitchenEquipment array too long`() {
        // given
        val settings = Settings(
            id = "id",
            budget = 40L,
            favoriteMeals = listOf("Meal1"),
            likedIngredients = listOf("Ingre"),
            dislikedIngredients = listOf("Ingre"),
            allergies = listOf("Allergy1"),
            dietaryPreferences = listOf("Dietary"),
            kitchenEquipment = listOf("Kitchen", "Kitchen2", "Kitchen3", "Kitchen4"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        // when
        val result = settingsValidationService.validateSettings(settings)

        // then
        assertThat(result).isPresent
        assertThat(result.get().size).isEqualTo(1)
        assertThat(result.get()).containsEntry("kitchenEquipment", "Kitchen exceeds the limit.")
    }
}