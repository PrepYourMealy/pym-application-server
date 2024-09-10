package app.prepmymealy.application.service

import app.prepmymealy.application.domain.Settings
import app.prepmymealy.application.domain.User
import app.prepmymealy.application.domain.UserLimits
import app.prepmymealy.application.domain.UserStats
import app.prepmymealy.application.repository.SettingsRepository
import app.prepmymealy.application.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Objects

@Service
class SettingsUpdateService(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
) {
    fun createSettings(settings: Settings): Boolean {
        val optionalDbSettings = settingsRepository.findById(settings.id)
        if (optionalDbSettings.isPresent) {
            return false
        }
        settingsRepository.save(settings)
        val optionalUser = userRepository.findById(settings.id)
        if (optionalUser.isEmpty) {
            val initUser = createDefaultUser(settings.id)
            userRepository.save(initUser)
        }

        return true
    }

    fun updateSettings(settings: Settings): Boolean {
        val optionalDbSettings = settingsRepository.findById(settings.id)
        if (optionalDbSettings.isEmpty) {
            return false
        }
        if (isEqual(settings, optionalDbSettings.get())) {
            return true
        }
        settingsRepository.save(settings)
        return true
    }

    private fun isEqual(
        update: Settings,
        existing: Settings,
    ): Boolean {
        return Objects.equals(update, existing)
    }

    private fun createDefaultUser(id: String): User {
        return User(
            id = id,
            stats = UserStats(weeklyRegenerateRequest = 0),
            limits = UserLimits(2),
        )
    }
}
