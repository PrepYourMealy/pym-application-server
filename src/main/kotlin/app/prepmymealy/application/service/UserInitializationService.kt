package app.prepmymealy.application.service

import app.prepmymealy.application.domain.settings.DailySettings
import app.prepmymealy.application.domain.settings.MenuSettings
import app.prepmymealy.application.domain.settings.ReducedWeeklySettings
import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.domain.user.User
import app.prepmymealy.application.domain.user.UserLimits
import app.prepmymealy.application.domain.user.UserStats
import org.springframework.stereotype.Service

@Service
class UserInitializationService(
    private val userService: UserService,
    private val settingsService: SettingsService,
) {
    companion object {
        private fun defaultUser(userId: String): User {
            return User.builder(userId)
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(2)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(0)
                        .build(),
                )
                .build()
        }

        private fun defaultSettings(userId: String): Settings {
            return Settings.builder(userId)
                .budget(40)
                .includeDiscounts(true)
                .overallWeeklySettings(
                    ReducedWeeklySettings.builder()
                        .overall(
                            DailySettings.builder()
                                .overall(
                                    MenuSettings.builder()
                                        .isActive(true)
                                        .people(2)
                                        .build(),
                                )
                                .build(),
                        )
                        .build(),
                )
                .build()
        }
    }

    fun initializeUser(userId: String) {
        val storedUser = userService.getUserById(userId)
        if (storedUser.isEmpty) {
            val user = defaultUser(userId)
            userService.createUser(user)
        }
        val storedSettings = settingsService.getSettingsById(userId)
        if (storedSettings.isEmpty) {
            val settings = defaultSettings(userId)
            settingsService.createSettings(settings)
        }
    }
}
