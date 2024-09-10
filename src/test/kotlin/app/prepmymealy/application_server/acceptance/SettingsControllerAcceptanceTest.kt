package app.prepmymealy.application_server.acceptance

import app.prepmymealy.application_server.acceptance.api.ControllerApi
import app.prepmymealy.application_server.domain.Settings
import app.prepmymealy.application_server.repository.SettingsRepository
import app.prepmymealy.application_server.testsupport.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SettingsControllerAcceptanceTest : AbstractSpringTest() {

    @Autowired
    private lateinit var api: ControllerApi

    @Autowired
    private lateinit var settingsRepository: SettingsRepository

    @Test
    fun `should give the correct settings object for given userid`() {
        // given
        val userId = "userId"
        val settings = Settings(
            id = userId,
            budget = 40L,
            favoriteMeals = listOf("Meal1", "Meal2"),
            likedIngredients = listOf("Ingredient1", "Ingredient2"),
            dislikedIngredients = listOf("Ingredient1", "Ingredient2"),
            allergies = listOf("Allergy1", "Allergy2"),
            dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2"),
            kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2"),
            includeDiscounts = true,
            people = 2,
            mealsPerDay = 2
        )
        settingsRepository.save(settings)
        // when
        val response = api.getSessionById(userId)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val expectedJson = "{" +
                "\"id\":\"$userId\"," +
                "\"budget\":40," +
                "\"favoriteMeals\":[\"Meal1\",\"Meal2\"]," +
                "\"likedIngredients\":[\"Ingredient1\",\"Ingredient2\"]," +
                "\"dislikedIngredients\":[\"Ingredient1\",\"Ingredient2\"]," +
                "\"allergies\":[\"Allergy1\",\"Allergy2\"]," +
                "\"dietaryPreferences\":[\"Dietary Preference1\",\"Dietary Preference2\"]," +
                "\"kitchenEquipment\":[\"KitchenEquipment1\",\"KitchenEquipment2\"]," +
                "\"includeDiscounts\":true," +
                "\"people\":2," +
                "\"mealsPerDay\":2" +
        "}"
        assertThat(response.body).isEqualTo(expectedJson)
    }

    @Test
    fun `should return 404 when no settings object found for user`() {
        // given
        val someId = "someUserId"
        // when
        val response = api.getSessionById(someId)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val expectedJson = "{" +
                "\"code\":404," +
                "\"message\":\"Settings not found for id: $someId\"" +
        "}"
        assertThat(response.body).isEqualTo(expectedJson)
    }
}