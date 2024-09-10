package app.prepmymealy.application.service

import app.prepmymealy.application.domain.Settings
import app.prepmymealy.application.repository.SettingsRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class SettingsService(private val settingsRepository: SettingsRepository) {
    fun getSettingsById(id: String): Optional<Settings> {
        return settingsRepository.findById(id)
    }
}
