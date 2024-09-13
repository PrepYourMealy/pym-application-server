package app.prepmymealy.application.acceptance

import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.repository.SettingsRepository
import app.prepmymealy.application.repository.UserRepository
import app.prepmymealy.application.testsupport.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class SettingsControllerAcceptanceTest : AbstractSpringTest() {
    @Autowired
    private lateinit var settingsRepository: SettingsRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeMethod
    fun setUp() {
        settingsRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `should give the correct settings object for given userid`() {
        // given
        val userId = "userId"
        val settings =
            Settings(
                id = userId,
                budget = 40L,
                favoriteMeals = listOf("Meal1", "Meal2"),
                likedIngredients = listOf("Ingredient1", "Ingredient2"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2"),
                allergies = listOf("Allergy1", "Allergy2"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2"),
                includeDiscounts = true,
            )
        settingsRepository.save(settings)
        // when
        val response = api.getSettingsById(userId)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val expectedJson =
            "{" +
                "\"id\":\"$userId\"," +
                "\"budget\":40," +
                "\"favoriteMeals\":[\"Meal1\",\"Meal2\"]," +
                "\"likedIngredients\":[\"Ingredient1\",\"Ingredient2\"]," +
                "\"dislikedIngredients\":[\"Ingredient1\",\"Ingredient2\"]," +
                "\"allergies\":[\"Allergy1\",\"Allergy2\"]," +
                "\"dietaryPreferences\":[\"Dietary Preference1\",\"Dietary Preference2\"]," +
                "\"kitchenEquipment\":[\"KitchenEquipment1\",\"KitchenEquipment2\"]," +
                "\"includeDiscounts\":true" +
                "}"
        assertThat(response.body).isEqualTo(expectedJson)
    }

    @Test
    fun `should return 404 when no settings object found for user`() {
        // given
        val someId = "someUserId"
        // when
        val response = api.getSettingsById(someId)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val expectedJson =
            "{" +
                "\"code\":404," +
                "\"message\":\"Settings not found for id: $someId\"" +
                "}"
        assertThat(response.body).isEqualTo(expectedJson)
    }

    @Test
    fun `should create settings and user data on post`() {
        // given
        val userId = "userId"
        val settings =
            Settings(
                id = userId,
                budget = 40L,
                favoriteMeals = listOf("Meal1", "Meal2"),
                likedIngredients = listOf("Ingredient1", "Ingredient2"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2"),
                allergies = listOf("Allergy1", "Allergy2"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2"),
                includeDiscounts = true,
            )
        // when
        val response = api.postSettings(settings)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        val user = userRepository.findById(userId)
        assertThat(user).isPresent
        user.ifPresent {
            it.limits?.let { it1 -> assertThat(it1.regenerateRequestsPerWeek).isEqualTo(2) }
            it.stats?.let { it1 -> assertThat(it1.weeklyRegenerateRequest).isEqualTo(0) }
        }
        val settingsFromDb = settingsRepository.findById(userId)
        assertThat(settingsFromDb).isPresent
        settingsFromDb.ifPresent {
            assertThat(it).isEqualTo(settings)
        }
    }

    @Test
    fun `should not create when already created`() {
        // given
        val userId = "userId"
        val settings =
            Settings(
                id = userId,
                budget = 40L,
                favoriteMeals = listOf("Meal1", "Meal2"),
                likedIngredients = listOf("Ingredient1", "Ingredient2"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2"),
                allergies = listOf("Allergy1", "Allergy2"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2"),
                includeDiscounts = true,
            )
        val updatedSettings =
            Settings(
                id = userId,
                budget = 50L,
                favoriteMeals = listOf("Meal1", "Meal2", "Meal3"),
                likedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3"),
                allergies = listOf("Allergy1", "Allergy2", "Allergy3"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2", "Dietary Preference3"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2", "KitchenEquipment3"),
                includeDiscounts = false,
            )
        settingsRepository.save(settings)
        // when
        val response = api.postSettings(updatedSettings)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val settingsFromDb = settingsRepository.findById(userId)
        assertThat(settingsFromDb).isPresent
        settingsFromDb.ifPresent {
            assertThat(it).isEqualTo(settings)
        }
    }

    @Test
    fun `should update settings on put`() {
        // given
        val userId = "userId"
        val settings =
            Settings(
                id = userId,
                budget = 40L,
                favoriteMeals = listOf("Meal1", "Meal2"),
                likedIngredients = listOf("Ingredient1", "Ingredient2"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2"),
                allergies = listOf("Allergy1", "Allergy2"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2"),
                includeDiscounts = true,
            )
        settingsRepository.save(settings)
        val updatedSettings =
            Settings(
                id = userId,
                budget = 50L,
                favoriteMeals = listOf("Meal1", "Meal2", "Meal3"),
                likedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3"),
                allergies = listOf("Allergy1", "Allergy2", "Allergy3"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2", "Dietary Preference3"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2", "KitchenEquipment3"),
                includeDiscounts = false,
            )
        // when
        val response = api.putSettings(userId, updatedSettings)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val settingsFromDb = settingsRepository.findById(userId)
        assertThat(settingsFromDb).isPresent
        settingsFromDb.ifPresent {
            assertThat(it).isEqualTo(updatedSettings)
        }
    }

    @Test
    fun `should not update settings on validation error`() {
        // given
        val userId = "userId"
        val settings =
            Settings(
                id = userId,
                budget = 40L,
                favoriteMeals = listOf("Meal1", "Meal2"),
                likedIngredients = listOf("Ingredient1", "Ingredient2"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2"),
                allergies = listOf("Allergy1", "Allergy2"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2"),
                kitchenEquipment = listOf("KitchenEquipment1", "KitchenEquipment2"),
                includeDiscounts = true,
            )
        settingsRepository.save(settings)
        val updatedSettings =
            Settings(
                id = userId,
                budget = 50L,
                favoriteMeals = listOf("Meal1", "Meal2", "Meal3"),
                likedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3"),
                dislikedIngredients = listOf("Ingredient1", "Ingredient2", "Ingredient3"),
                allergies = listOf("Allergy1", "Allergy2", "Allergy3"),
                dietaryPreferences = listOf("Dietary Preference1", "Dietary Preference2", "Dietary Preference3"),
                kitchenEquipment =
                    listOf(
                        "KitchenEquipment1KitchenEquipment1KitchenEquipment1KitchenEquipment1KitchenEquipment1KitchenEquipment1KitchenEquipment1",
                        "KitchenEquipment2",
                        "KitchenEquipment3",
                    ),
                includeDiscounts = false,
            )
        // when
        val response = api.putSettings(userId, updatedSettings)
        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val settingsFromDb = settingsRepository.findById(userId)
        assertThat(settingsFromDb).isPresent
        settingsFromDb.ifPresent {
            assertThat(it).isEqualTo(settings)
        }
    }
}
